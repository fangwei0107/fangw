package com.chinawyny.grammer

trait Dao {

    var a : String

    var b : String = "b"

    def delete(id: String) = println("delete")

    def add(id: String) : Unit

}



abstract class Abs(val z: String) {
    self: Dao =>
}