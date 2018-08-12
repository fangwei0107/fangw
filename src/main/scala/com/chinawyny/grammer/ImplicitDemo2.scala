package com.chinawyny.grammer

import scala.util.Random

object ImplicitDemo2 {
    def main(args: Array[String]): Unit = {
        def m1(x: =>Int) = List(x, x)
        var r = new Random()
        m1(r.nextInt()).foreach(println)
    }
}
class Animal {}
class Bird extends Animal {}
class Consumer[-S,+T]()(implicit m1:Manifest[T]) {
    def m1[U >: T](u: U): T = {m1.runtimeClass.newInstance.asInstanceOf[T]} //协变，下界
    def m2[U <: S](s: S)(implicit m2:Manifest[U]): U = {m1.runtimeClass.newInstance.asInstanceOf[U]} //逆变，上界
}