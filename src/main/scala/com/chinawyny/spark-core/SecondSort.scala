package com.chinawyny.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 二次排序，排完一列排第二列
  *
  * @author
  */
object SecondSort {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("SecondSort").setMaster("local[*]")
        val sc = new SparkContext(conf)
        val lines = sc.textFile("second_sort.txt")
        val pairs = lines.map(line => {
            val words = line.split(" ")
            Tuple2(SecondSortKey(words(0).toInt, words(2).toInt), line)
        })

        val sortedPairs = pairs.sortByKey(ascending = false)
        val result = sortedPairs.map(_._2).collect()
        result.foreach(println)
    }
}

case class SecondSortKey(firstKey: Int, secondKey: Int) extends Serializable with Ordered[SecondSortKey] {
    override def compare(that: SecondSortKey) = {
        if (firstKey == that.firstKey) {
            -secondKey.compareTo(that.secondKey)
        } else {
            firstKey.compareTo(that.firstKey)
        }
    }
}