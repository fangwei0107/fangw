package com.chinawyny.grammer

object ReturnDemo {

    def main(args: Array[String]): Unit = {
        println(m1(-1))
    }

    val m1: Int => Int = {
        x:Int => {
           if (x > 0) {
               1
           }
            -1
        }
    }

}
