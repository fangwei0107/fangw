package com.chinawyny.spark_sql

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object DataSetDemo {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().master("local").appName("DataSetDemo").getOrCreate()
        import spark.implicits._
//        //用包含case class的RDD创建dateframe
//        val peopleDF = spark.sparkContext.textFile("people2.txt").map(_.split(",")).map(p => Person(p(0), p(1).trim.toInt)).toDF()
//        peopleDF.createTempView("people")
//        val peopleDF1 = spark.sparkContext.textFile("people3.txt").map(_.split(",")).map(p => Person1(p(0), p(1).trim.toInt, p(2))).toDF()
//        peopleDF1.createTempView("people1")
//
//        peopleDF.map(r => r.getString(0)).show()
//        val olderDF = spark.sql(
//            "select p.name as p_name,p1.name as p1_name, p1.age as p1_age from people p inner join people1 p1 on p.age = p1.age where p1.age >= 18")
//        olderDF.show()
//        olderDF.printSchema()
//
//        val peopleDS1 = peopleDF1.as[Person1]
//        peopleDS1.select("name").show()
//
//        //用Schema创建dataframe
//        val schemaString = "name age"
//        val schema = StructType(schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))
//        val peopleRDD = spark.sparkContext.textFile("people2.txt").map(_.split(",")).map(p => Row(p(0), p(1).trim))
//        println("schemaDF")
//        spark.createDataFrame(peopleRDD, schema).write.saveAsTable("people2")
        spark.table("people2").show()


    }

    private def sparkSQLDemo2(spark: SparkSession) = {
        val rdd = spark.sparkContext.parallelize(Seq(("a", 1), ("b", 1), ("a", 1)))
        import spark.implicits._
        var flag = 0
        val test = rdd.mapPartitions {
            part => {
                part.map { line =>
                    println("运行")
                    flag += 1
                    println(flag)
                    line._1
                }
            }
        }
        println(test.count())
        println(flag)
    }

    private def dataSetDemo1(spark: SparkSession, peopleDF: DataFrame) = {
        import spark.implicits._
        //        转换成DataSet
        val peopleDS = peopleDF.as[People]
        peopleDF.createTempView("people")
        //        val peopleDF2 = spark.sql("select name from people where age > 15")
        //        println("peopleDF2")
        //        peopleDF2.show()
        peopleDS.show()

        val numDS = spark.range(5, 100, 5)
    }
}

case class People(id: BigInt, name: String, age: BigInt)

case class Coltest(col1: String, col2: Int) extends Serializable //定义字段名和类型

case class Person(name: String, age: Int)

case class Person1(name: String, age: Int, sex: String)
