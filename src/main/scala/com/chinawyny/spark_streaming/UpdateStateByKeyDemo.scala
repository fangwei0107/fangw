package com.chinawyny.spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

/**
  * SparkStream读取的是DStream,其中每一个时间间隔是一个RDD，
  * 普通的RDD算子操作并没有跨越时间间隔，需要保留不同时间间隔数据的状态需要使用updateStateByKey(func)
  *
  * @author fangwei
  */
object UpdateStateByKeyDemo {

    Logger.getLogger("org").setLevel(Level.WARN)

    def main(args: Array[String]): Unit = {
        def main(args: Array[String]) {
            if (args.length < 2) {
                System.err.println("Usage: UpdateStateByKeyDemo <hostname> <port>")
                System.exit(1)
            }
        }

        val sparkConf = new SparkConf().setAppName("UpdateStateByKeyDemo").setMaster("local[*]")

        val ssc = new StreamingContext(sparkConf, Seconds(5))

        // 使用UpdateStateByKey需要设置checkpoint目录
        ssc.checkpoint("statecheckpoint")

        val initialRDD = ssc.sparkContext.parallelize(List(("hello", 1), ("world", 1)))

        val lines = ssc.socketTextStream(args(0), args(1).toInt)

        val mappingFunc: (String, Option[Int], State[Int]) => (String, Int) = (device: String, nowWp: Option[Int], lastWp: State[Int]) => {
            // 如果历史wp为则更新
            if (lastWp.getOption().isEmpty) {
                lastWp.update(1)
                (device, -1)
            } else {
                // 如果历史wp不为空，则进行计算，更新状态
                val elec = nowWp.get - lastWp.getOption().getOrElse(0)
                lastWp.update(nowWp.getOrElse(0))
                (device, elec)
            }
        }

        val deviceWp = lines.map(x => {
            val deviceWp = x.split(" ")
            (deviceWp(0), deviceWp(1).toInt)
        }).mapWithState(StateSpec.function(mappingFunc)).filter(_._2 != -1)
        deviceWp.print()
        deviceWp.foreachRDD(rdd => {
            rdd.foreachPartition(partition => {
                if (partition.nonEmpty) {
                    println(partition.toList)
                }
            })
        })

        ssc.start()
        ssc.awaitTermination()

    }

}
