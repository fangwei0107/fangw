package com.chinawyny.grammer

import java.io.File

import scala.io.Source

object ImplicitDemo {

    def main(args: Array[String]): Unit = {
        trait S1{
            def fun() :Unit
        }

        implicit object S2 extends S1 {
            
            override def fun(): Unit = println("ok")
        }
        def g(arg: String)(implicit arg2 : S1) = println("隐式对象解析")
        g("scala")(new S1{
            override def fun(): Unit = println("spark")
        })
        g("scala")
    }

}

object S1{
    implicit class RichFile(file: File) {
        def read(): String = Source.fromFile(file.getPath).mkString
    }
}
