package com.chinawyny.grammer

object ClassDemo {
    def main(args: Array[String]): Unit = {
//        val a = new Person1
//        println(a.getName)
//
//        val b = new Person1("b", 18)
//        println(b.getName)
//
//        val c = new Person2("1", 1, 1)
//        c.name = "2"
        println(new Man().env.length)
    }
}

class Person1 private() {

    private var name: String = _

    private var age: Int = 0
    // 实际上this是重载了主构造器,使用new Person()依然可以创建对象
    // 默认构造器，即主构造器，class Person实际上省略了(),class Person()是主构造器
    def this(name: String, age: Int) {
        this
        this.name = name
        this.age = age
    }

    def getName = name
    def setName(name: String) = {this.name = name}
}

/**
  * 类自动为public
  * 在Scala中任何没有标记为private或则protected的数据都默认为public。
  *
  * @param name
  * @param age
  * @param sex
  */
class Person2(var name: String, val age: Int, sex: Int){
}


class Human {
    val length: Int = 10
    val env: Array[Int] = new Array[Int](length)
}

class Man extends {
    override val length: Int = 2
} with Human