package com.chinawyny.spark_core

import org.apache.spark.{SparkConf, SparkContext}

object Main {
    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName("test").setMaster("local[*]")
        val javaSparkContext = new SparkContext(conf)

        val rdd = javaSparkContext.parallelize(Range(1, 10000), 10)

        rdd.mapPartitions(i => {
            val r = i.map(Util.process)
            r
        }) // 序列化错误

    }
}

object Util {
    def process(i: Int): Int = i + 1
}