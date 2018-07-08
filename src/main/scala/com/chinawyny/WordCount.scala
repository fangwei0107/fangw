package com.chinawyny

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
    def main(args: Array[String]) {

        Logger.getLogger("org").setLevel(Level.WARN)
        val conf = new SparkConf().setAppName("WordCount").setMaster("local")
        val sc = new SparkContext(conf)
        val textFile = sc.textFile("text.txt")
        val words = textFile.flatMap(line => line.split(" "))
        val wordPairs = words.filter(!_.equals(""))map(word => (word, 1))
        val wordCounts = wordPairs.reduceByKey((a,b) => a + b)
        println("wordCounts: ")
        wordCounts.collect().foreach(println)

    }
}