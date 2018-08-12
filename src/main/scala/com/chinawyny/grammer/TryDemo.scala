package com.chinawyny.grammer

import java.net.URL

import scala.util.Try

object TryDemo {
    def main(args: Array[String]): Unit = {
        def parseURL(url: String): Try[URL] = Try(new URL(url))
        println(parseURL("http://danielwestheide.com").map(_.getProtocol))
        // results in Success("http")
        println(parseURL("garbage").map(_.getProtocol))
        // results in Failure(java.net.MalformedURLException: no protocol: garbage)
    }
}
