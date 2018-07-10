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

        val ssc = new StreamingContext(sparkConf, Seconds(2))

        //使用UpdateStateByKey需要设置checkpoint目录
        ssc.checkpoint("./statecheckpoint")

        val initialRDD = ssc.sparkContext.parallelize(List(("hello", 1), ("world", 1)))

        val lines = ssc.socketTextStream(args(0), args(1).toInt)

        val words = lines.flatMap(_.split(" "))
        val wordCountDstream = words.map(x => (x, 1)).reduceByKey(_ + _)

        //更新(word,count)，保留count为状态，跨时间间隔的累加
        val mappingFunc = (word: String, one: Option[Int], state: State[Int]) => {
            val sum = one.getOrElse(0) + state.getOption.getOrElse(0)
            val output = (word, sum)
            state.update(sum)
            output
        }

        val stateDstream = wordCountDstream.mapWithState(StateSpec.function(mappingFunc).initialState(initialRDD))
        stateDstream.print()

        ssc.start()
        ssc.awaitTermination()

    }

}
