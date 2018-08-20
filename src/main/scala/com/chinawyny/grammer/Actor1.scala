package com.chinawyny.grammer

import scala.actors.Actor

object Actor1 extends Actor {
    override def act(): Unit = {
        for (i <- 1 to 10) {
            println("actor-1 " + i)
            Thread.sleep(2000)
        }
    }
}
object Actor2 extends Actor {
    override def act(): Unit = {
        for (i <- 1 to 10) {
            println("actor-2 " + i)
            Thread.sleep(2000)
        }
    }
}
object ActorTest extends App{
    //启动Actor
    Actor1.start()
    Actor2.start()
}
