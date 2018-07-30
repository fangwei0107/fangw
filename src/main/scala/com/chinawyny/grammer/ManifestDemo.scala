package com.chinawyny.grammer

import scala.reflect.ClassTag

class ManifestDemo[T](implicit x: ClassTag[T]) {

    def makeTArray(): Array[T] = new Array[T](10)

    def makeTArray2() = {
        if (x <:< manifest[String]) {
            println("String")
        } else {
            println("other")
        }
    }

    def makeStringArray(): Array[String] = new  Array[String](10)


}

object ManifestDemo extends App {

    val c = new ManifestDemo[String]
    c.makeTArray2()

    val pair = new Pair[String, String]("1","2").first
    println(pair)

}

class Pair[T, S](val first: T, val second: S) {


    def getFirst() = first

}
