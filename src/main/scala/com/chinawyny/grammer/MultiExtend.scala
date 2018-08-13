package com.chinawyny.grammer

object MultiExtend {
    def main(args: Array[String]): Unit = {
        val c = new C
        println(c.a)
        c.hello
    }

}

trait A {
    val a = "a"
    def hello  = {
        println("helloa")
    }
}

trait B {
    val a = "b"
    def hello  = {
        println("hellob")
    }
}

class C extends A with B {
    override val a: String = "a"

    override def hello: Unit = super[B].hello
}
