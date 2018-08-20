package com.chinawyny.grammer

import scala.actors.Actor

case class SyncMessage(id:Int,msg:String)//同步消息
case class AsyncMessage(id:Int,msg:String)//异步消息
case class ReplyMessage(id:Int,msg:String)//返回结果消息


class MsgActor extends Actor{
    override def act(): Unit = {
        loop {
            react {
                case "start" =>
                    println("starting")
                case SyncMessage(id, msg) =>
                    println(s"id $id,SyncMessage: $msg")
                    Thread.sleep(2000)
                    sender !ReplyMessage(1, "finish...")
                case AsyncMessage(id, msg) =>
                    println(s"id $id,AsyncMessage: $msg")
                    sender !ReplyMessage(3, "finish...")
                    Thread.sleep(2000)

            }
        }
    }
}

object MsgActor {
    def main(args: Array[String]): Unit = {
        val msgActor = new MsgActor
        msgActor.start()
        msgActor !"start"

        // ！？同步消息，有返回值
        val reply1 = msgActor !? SyncMessage(1, "同步消息")
        println(reply1)
        println("===============================")

        // 异步无返回消息
        val reply2 = msgActor ! AsyncMessage(2, "异步无返回消息")
        println(reply2) //输出()
        println("===============================")

        // Future的apply()方法会构建一个异步操作且在未来某一个时刻返回一个值
        val reply3 = msgActor !! AsyncMessage(3, "异步有返回消息")
        println(reply3.apply())
    }
}
