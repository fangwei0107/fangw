package com.chinawyny.grammer

object LazyDemo {

    def main(args: Array[String]): Unit = {
        println("Start")

        val fun = () => println(1)
        val startTime = System.currentTimeMillis()
        val person = new Person
        val endTime = System.currentTimeMillis()

        println("End and take " + (endTime - startTime) + "ms")

        person.properties

    }

}

class Person {
    lazy val properties = {
        println("init")
        Thread.sleep(2000)
    }
}
