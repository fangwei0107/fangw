package com.chinawyny.grammer

object ImplicitDemo1 {
    def main(args: Array[String]): Unit = {
        def bigger1[T <% Ordered[T]](x: T, y: T): T = {
            if (x > y)
                x
            else
                y
        }
        println(bigger1(10, 20))

        def bigger2[T](x: T,y: T)(implicit m: T=> Ordered[T]): T = {
            if (x > y)
                x
            else
                y
        }
        println(bigger2(10,30))

        def bigger3[T: Ordering](x: T,y: T): T = {
            if (Ordering[T].compare(x, y) > 0)
                x
            else
                y
        }
        println(bigger3(10,15))

        def bigger4[T](x: T,y: T)(implicit v: Ordering[T]): T = {
            if (Ordering[T].compare(x, y) > 0)
                x
            else
                y
        }

        implicit object v extends Ordering[Int] {
            override def compare(x: Int, y: Int) = {
                if (x > y)
                    -1
                else
                    1
            }
        }
        implicit val a = 1
        println(implicitly[Int])

        println(bigger4(10,11)(v))

    }
}
