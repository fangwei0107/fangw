package com.chinawyny.spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}


// 使用 nc -l -p 9997 建立windows发送数据窗口
object StreamingDemo {
    def main(args: Array[String]): Unit = {
        Logger.getLogger("org").setLevel(Level.WARN)
        val conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("NetworkWordCount")

        val ssc = new StreamingContext(conf, Seconds(5))

        ssc.sparkContext.broadcast()

        val lines = ssc.socketTextStream("localhost",9999)

        val words = lines.flatMap(_.split(" "))

        val pairs = words.map(word => (word, 1))

        val wordCount = pairs.reduceByKey(_ + _)

        wordCount.foreachRDD(rdd => {
            val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
            import spark.implicits._
            val wordCountDF = rdd.toDF("value", "count")
            wordCountDF.createOrReplaceTempView("wordcount")
            spark.sql("select * from wordcount where count > 1").show()

        })

        wordCount.print()

        ssc.start()
        ssc.awaitTermination()

    }
}

case class WordCount(value: String, count: Int)
