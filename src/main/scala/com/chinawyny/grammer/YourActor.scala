package com.chinawyny.grammer

import scala.actors.Actor

class YourActor extends Actor {
    override def act(): Unit = {
        loop(
            react {
                case "start" =>
                    println("starting ...")

                case "stop" =>
                    println("stopping ...")
            }
        )
    }
}

object YourActor {
    def main(args: Array[String]): Unit = {
        val actor = new YourActor
        actor.start()
        actor ! "start"
        actor ! "stop"
        println("消息发送完成！")
    }
}
