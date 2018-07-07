package com.chinawyny.spark_sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}

object SparkSqlDemo {

    def main(args: Array[String]): Unit = {
        val sql = SparkSession.builder()
                .appName("SparkSqlDemo")
                .master("local")
                .getOrCreate()
        val people = sql.read.json("people.txt")
        println(people)
//        println(people.schema)
//        people.columns.foreach(println)
        people.createTempView("people")
        val res = sql.sql(
            """
              |select name
              |from people
              |where age > 11
            """.stripMargin)
        res.foreach(r => println(r.getString(0)))
    }

}
