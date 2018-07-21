package com.chinawyny.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * group top n和top n
  */
object TopN {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("TopN").setMaster("local[*]")
        val sc = new SparkContext(conf)
        val lines = sc.textFile("group_top_n.txt")
        val pairs = lines.map(line => {
            val words = line.split(" ")
            (words(0), words(1).toInt)
        })
        val pairGroup = pairs.groupByKey()
        val res = pairGroup.map(pair => {
            val values = pair._2
            val sortValues = values.toList.sortWith(_>_).take(2)
            (pair._1, sortValues)
        }).collect()
        res.foreach(println)
    }

    private def topN(sc: SparkContext) = {
        val lines = sc.textFile("topN.txt")
        val pairs = lines.map(line => Tuple2(line.toInt, line))
        val results = pairs.sortByKey(ascending = false).map(_._2).take(3)
        results.foreach(println)
    }
}
