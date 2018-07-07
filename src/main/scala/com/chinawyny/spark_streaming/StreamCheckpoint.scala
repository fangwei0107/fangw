package com.chinawyny.spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}


object StreamCheckpoint {

    def functionToCreateContext():StreamingContext={
        def updateStateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
            Some(runningCount.getOrElse(0) + newValues.sum)
        }

        val conf = new SparkConf().setMaster("local[5]").setAppName("AndrzejApp")
        val ssc = new StreamingContext(conf, Seconds(4))
        ssc.checkpoint("tmp")

        val line = ssc.socketTextStream("127.0.0.1", 9997)
        val words=line.flatMap(_.split(" "))

        val pairs=words.map((_,1))

        val wordCount = pairs.reduceByKey(_ + _)

        wordCount.foreachRDD(rdd => {
            val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
            import spark.implicits._
            val wordCountDF = rdd.toDF("value", "count")
            wordCountDF.createOrReplaceTempView("wordcount")
            spark.sql("select * from wordcount where count > 1").show()

        })

        wordCount.print()
        val retDS = pairs.updateStateByKey[Int](updateStateFunction _)

        retDS.repartition(1).saveAsTextFiles("tmp/out")
        retDS.print()

        ssc
    }



    def main(args: Array[String]): Unit = {
//        def functionToCreateContext(): StreamingContext = {
//
//            def updateStateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
//                Some(runningCount.getOrElse(0) + newValues.sum)
//            }
//
//            Logger.getLogger("org").setLevel(Level.WARN)
//            val conf = new SparkConf()
//                    .setMaster("local[2]")
//                    .setAppName("NetworkWordCount")
//            val ssc = new StreamingContext(conf, Seconds(5))
//
//
//            val lines = ssc.socketTextStream("localhost",9999) // 创建DStreams
//            val words = lines.flatMap(_.split(" "))
//
//            val pairs = words.map(word => (word, 1))
//
//            val wordCount = pairs.reduceByKey(_ + _)
//
//            wordCount.foreachRDD(rdd => {
//                val spark = SparkSession.builder().config(ssc.sparkContext.getConf).getOrCreate()
//                import spark.implicits._
//                val wordCountDF = rdd.toDF("value", "count")
//                wordCountDF.createOrReplaceTempView("wordcount")
//                spark.sql("select * from wordcount").show()
//            })
//
//            val retDS = pairs.updateStateByKey[Int](updateStateFunction _)
//            retDS.repartition(1).saveAsTextFiles("checkpoint/out")
//            retDS.print()
//
//            ssc.checkpoint("checkpoint/")   // 设置好检查点目录
//            ssc
//        }
        Logger.getLogger("org").setLevel(Level.WARN)
        val ssc = StreamingContext.getOrCreate("tmp/", functionToCreateContext _)


        ssc.start()
        ssc.awaitTermination()

    }
}
