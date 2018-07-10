package com.chinawyny.spark_streaming

import java.nio.charset.Charset

import com.google.common.io.Files
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.util.LongAccumulator

/**
  * 累加器，全局变量使用的Demo，使用双检查单例模式获取
  * 累加器（Accumulators）和广播变量（Broadcast variables）是无法从Spark Streaming的检查点中恢复回来的。
  * 所以如果你开启了检查点功能，并同时在使用累加器和广播变量，那么最好是使用懒惰实例化的单例模式
  *
  * @author fangwei
  */
object RecoverableNetworkWordCount {

    Logger.getLogger("org").setLevel(Level.WARN)

    def createContext(ip: String, port: Int, outputPath: String, checkpointDirectory: String): StreamingContext = {
        println("Creating new context")
        val outputFile = new java.io.File(outputPath)
        if (outputFile.exists) outputFile.delete()
        val sparkConf = new SparkConf().setAppName("RecoverableNetworkWordCount").setMaster("local[*]")
        val ssc = new StreamingContext(sparkConf, Seconds(1))
        ssc.checkpoint(checkpointDirectory)

        val lines = ssc.socketTextStream(ip, port)
        val words = lines.flatMap(line => line.split(" ")).filter(!_.equals(""))
        val wordCounts = words.map(word => (word, 1))

        wordCounts.foreachRDD {
            (rdd: RDD[(String, Int)], time: Time) => {
                val blackList = WordBlackList.getInstance(rdd.sparkContext)
                val droppedWordsCounter = DroppedWordsCounter.getInstance(rdd.sparkContext)

                val counts = rdd.filter {
                    case (word, count) =>
                        if (blackList.value.contains(word)) {
                            droppedWordsCounter.add(count)
                            false
                        } else {
                            true
                        }
                }.collect().mkString("[", ",", "]")
                val output = s"Counts at time $time $counts"
                println(output)
                println(s"Dropped ${droppedWordsCounter.value} word(s) totally")
                println(s"Appending to ${outputFile.getAbsolutePath}")
                Files.append(output + "\n", outputFile, Charset.defaultCharset())
            }
        }
        ssc

    }

    def main(args: Array[String]): Unit = {
        if (args.length != 4) {
            System.err.println(s"Your arguments were ${args.mkString("[", ", ", "]")}")
            System.err.println(
                """
                  |Usage: RecoverableNetworkWordCount <hostname> <port> <checkpoint-directory>
                  |     <output-file>. <hostname> and <port> describe the TCP server that Spark
                  |     Streaming would connect to receive data. <checkpoint-directory> directory to
                  |     HDFS-compatible file system which checkpoint data <output-file> file to which the
                  |     word counts will be appended
                  |
                  |In local mode, <master> should be 'local[n]' with n > 1
                  |Both <checkpoint-directory> and <output-file> must be absolute paths
                """.stripMargin
            )
            System.exit(1)
        }

        val Array(ip, port, outputPath, checkpointDirectory) = args

        val ssc = StreamingContext.getOrCreate(checkpointDirectory, () => createContext(ip, port.toInt, outputPath, checkpointDirectory))

        ssc.start()
        ssc.awaitTermination()
    }
}

/**
  * 广播变量，在广播时就获得变量，各个worker在工作节点上使用
  */
object WordBlackList {

    @volatile private var instance: Broadcast[Seq[String]] = _

    def getInstance(sc: SparkContext): Broadcast[Seq[String]] = {
        if (instance == null) {
            synchronized {
                if (instance == null) {
                    val wordBlackList = Seq("a", "b", "c")
                    instance = sc.broadcast(wordBlackList)
                }
            }
        }
        instance
    }
}

/**
  * 累加器，
  */
object DroppedWordsCounter {

    @volatile private var instance: LongAccumulator = _

    def getInstance(sc: SparkContext): LongAccumulator = {
        if (instance == null) {
            synchronized {
                if (instance == null) {
                    instance = sc.longAccumulator("WordsInBlacklistCounter")
                }
            }
        }
        instance
    }
}