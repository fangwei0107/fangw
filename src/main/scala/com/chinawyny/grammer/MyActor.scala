package com.chinawyny.grammer

import scala.actors.Actor

class MyActor extends Actor{
    override def act(): Unit = {
        println("hello")
        while (true){
            receive {
                case "start" =>
                    println("starting ...")
                case "stop" =>
                    println("stopping ...")
            }
        }

    }
}

object MyActor extends App {
    val actor = new MyActor
    actor.start()
    actor ! "start"
    actor ! "stop"

    println("消息发送完成")
}
