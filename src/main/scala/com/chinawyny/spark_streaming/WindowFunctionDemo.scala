package com.chinawyny.spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 窗口函数Demo
  *
  * @author fangwei
  */
object WindowFunctionDemo {


    Logger.getLogger("org").setLevel(Level.WARN)

    def main(args: Array[String]): Unit = {
        def main(args: Array[String]) {
            if (args.length < 2) {
                System.err.println("Usage: WindowFunctionDemo <hostname> <port>")
                System.exit(1)
            }
        }

        val sparkConf = new SparkConf().setAppName("UpdateStateByKeyDemo").setMaster("local[*]")

        val ssc = new StreamingContext(sparkConf, Seconds(2))

        val lines = ssc.socketTextStream(args(0), args(1).toInt)

        val words = lines.flatMap(_.split(" "))
        val wordCount = words.map(x => (x, 1)).reduceByKey(_ + _)
        val windowStream1 = wordCount.window(Seconds(2))
        val windowStream2 = wordCount.window(Seconds(4))
        val joinStream = windowStream1.rightOuterJoin(windowStream2)
        joinStream.print()

        ssc.start()
        ssc.awaitTermination()
    }

    private def reduceByKeyAndWindowFun(words: DStream[String]) = {
        val windowWordCount = words.map(x => (x, 1)).reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(6), Seconds(2))
        windowWordCount.print()
    }
}
