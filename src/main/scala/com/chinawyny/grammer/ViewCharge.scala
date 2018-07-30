package com.chinawyny.grammer

object ViewCharge {

    def bigger[T](first: T, second: T)(implicit x: T => Comparable[T]): Unit = {
        if (first.compareTo(second) > 0) {
            println(first)
        } else {
            println(second)
        }
    }

    def main(args: Array[String]): Unit = {
        bigger(1,2)
    }

}
