package com.chinawyny.grammer

object HelloWorld extends Tt {

    var y = 2
    def main(args: Array[String]): Unit = {
        val mixin = Mixin("jijiang")
        mixin.foo("jijiang: ")
    }

    override def a: Unit = ???
}
class HelloWorld {
    val x = HelloWorld.y

    def say ={}

}

trait Tt {
    def a
    def b = {println("a")}
}
trait jijiang {
    def foo(msg: String) = println(msg)
}

trait mama extends jijiang {
    val str1 = "mama: "
    override def foo(msg: String) = println(str1.concat(msg))
}

trait papa extends jijiang {
    val str2 = "papa: "
    override def foo(msg: String) = println(str2 + msg)
}

class Mixin private(msg: String) extends jijiang {
    def this() = this("mixin")
}

object Mixin {
    // 如果包含菱形问题，则只执行最右边的
    def apply(msg: String) = new Mixin(msg) with papa with mama
}