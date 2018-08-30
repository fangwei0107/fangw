package com.chinawyny.grammer.keyword

import java.net.{MalformedURLException, URL}

object ExceptionDemo {

    def main(args: Array[String]): Unit = {
        def urlFor(path: String): URL =
            try {
                new URL(path)
            } catch {
                case e: MalformedURLException =>
                    new URL("http://www.scalalang.org")
            }

        println(urlFor("w"))
    }

}
