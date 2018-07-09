package com.chinawyny.spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
  * 使用spark流式处理，监听端口，形成text文本，并分析词频
  *
  * 使用 nc -l -p 9999 建立windows发送数据窗口
  *
  * @author fangwei
  * */
object StreamingDemo {

    Logger.getLogger("org").setLevel(Level.WARN)

    def main(args: Array[String]): Unit = {

        if (args.length < 2) {
            System.err.println("Usage: NetworkWordCount <hostname> <port>")
            System.exit(1)
        }

        // appName 是你给该应用起的名字，这个名字会展示在Spark集群的web UI上。
        // 而 master 是Spark, Mesos or YARN cluster URL，如果支持本地测试，你也可以用”local[*]”为其赋值。
        // 在实际工作中，你不应该将master参数硬编码到代码里，而是应用通过spark-submit的参数来传递master的值,
        // 通过master不同，改变任务调度器
        val conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("NetworkWordCount")

        // StreamingContext还有另一个构造参数，即：批次间隔，这个值的大小需要根据应用的具体需求和可用的集群资源来确定。
        // 为您的应用程序找出正确的批处理大小的一个好方法是用一个保守的批处理间隔（比如5-10秒）和低数据速率来测试它。
        // 为了验证系统是否能够跟上数据速率，您可以检查每个处理批处理的端到端延迟的值（在Spark驱动log4j日志中寻找“完全延迟”，或者使用StreamingListener接口）
        // 例如日志：18/07/09 14:27:05 INFO JobScheduler: Total delay: 0.399 s for time 1531117625000 ms (execution: 0.374 s)
        val ssc = new StreamingContext(conf, Seconds(5))
        ssc.sparkContext.broadcast()

        //利用StreamingContext，创建一个DStream，该DStream代表从前面的TCP数据源流入的数据流，同时TCP数据源是由主机名（如：hostname）和端口（如：9999）来描述的。
        val lines = ssc.socketTextStream(args(0), args(1).toInt, StorageLevel.MEMORY_AND_DISK)

        //flatMap 是一种 “一到多”（one-to-many）的映射算子，它可以将源DStream中每一条记录映射成多条记录
        val words = lines.flatMap(_.split(" ")).filter(!_.equals(""))

        val pairs = words.map(word => (word, 1))

        val wordCount = pairs.reduceByKey(_ + _)

        //        wordCount.foreachRDD(rdd => {
        //            val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
        //            import spark.implicits._
        //            val wordCountDF = rdd.toDF("value", "count")
        //            wordCountDF.createOrReplaceTempView("wordcount")
        //            spark.sql("select * from wordcount where count > 1").show()
        //
        //        })

        wordCount.print()

        // 调用streamingContext.start() 启动接收并处理数据。
        ssc.start()
        // 调用streamingContext.awaitTermination() 等待流式处理结束（不管是手动结束，还是发生异常错误）
        // 可以主动调用 streamingContext.stop() 来手动停止处理流程。
        ssc.awaitTermination()

        /**
          * 一旦streamingContext启动，就不能再对其计算逻辑进行添加或修改。
          * 单个JVM虚机同一时间只能包含一个active的StreamingContext。
          * StreamingContext.stop() 也会把关联的SparkContext对象stop掉，如果不想把SparkContext对象也stop掉，可以将StreamingContext.stop的可选参数 stopSparkContext 设为false。
          * 一个SparkContext对象可以和多个StreamingContext对象关联，只要先对前一个StreamingContext.stop(sparkContext=false)，然后再创建新的StreamingContext对象即可。
          */
    }
}

case class WordCount(value: String, count: Int)
