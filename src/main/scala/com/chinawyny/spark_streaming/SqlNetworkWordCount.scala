package com.chinawyny.spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}

/**
  * 用DataFrame和SQL对流数据进行分析
  *
  * @author fangwei
  */
object SqlNetworkWordCount {

    Logger.getLogger("org").setLevel(Level.WARN)

    def main(args: Array[String]) {
        if (args.length < 2) {
            System.err.println("Usage: NetworkWordCount <hostname> <port>")
            System.exit(1)
        }

        val sparkConf = new SparkConf().setAppName("SqlNetworkWordCount").setMaster("local[*]")
        val ssc = new StreamingContext(sparkConf, Seconds(2))

        val lines = ssc.socketTextStream(args(0), args(1).toInt, StorageLevel.MEMORY_AND_DISK)
        val words = lines.flatMap(_.split(" ")).filter(!_.equals(""))

        words.foreachRDD((rdd: RDD[String], time: Time) => {
            val spark = SparkSessionSingleton.getInstance(sparkConf)
            import spark.implicits._
            val wordsDataFrame = rdd.map(word => Record(word)).toDF()
            wordsDataFrame.createOrReplaceTempView("words")
            val wordsCountDataFrame = spark.sql("select word, count(*) as total from words group by word")
            println(s"========= ${time.toString()} =========")
            wordsCountDataFrame.show()
        })

        ssc.start()
        ssc.awaitTermination()
    }

}

case class Record(word: String)

/**
  * 懒加载SparkSession
  */
object SparkSessionSingleton {
    @transient private var instance: SparkSession = _

    def getInstance(sparkConf: SparkConf): SparkSession = {
        if (instance == null) {
            instance = SparkSession
                    .builder()
                    .config(sparkConf)
                    .getOrCreate()
        }
        instance
    }
}
