package com.chinawyny

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
    def main(args: Array[String]) {

        val conf = new SparkConf().setAppName("WordCount").setMaster("local")
        val sc = new SparkContext(conf)
        val textFile = sc.textFile("file:///C:/Users/fangw/IdeaProjects/spark-demo/text.txt")
        val words = textFile.flatMap(line => line.split(" "))
        val wordPairs = words.map(word => (word, 1))
        val wordCounts = wordPairs.reduceByKey((a,b) => a + b)
        println("wordCounts: ")
        wordCounts.collect().foreach(println)


    }
}
