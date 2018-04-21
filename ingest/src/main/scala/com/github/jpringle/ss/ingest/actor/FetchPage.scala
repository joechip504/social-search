package com.github.jpringle.ss.ingest.actor

import akka.actor.Status.Failure
import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import twitter4j._

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

object FetchPage {
  case class Page(xs: List[Status])

  def props(twitter: Twitter, user: Long, page: Int, size: Int = 100): Props = {
    Props(new FetchPage(twitter, user, page, size))
  }
}

/**
  * Make one call to [[twitter.getUserTimeline()]]
  * On success, wrap up the result and send it along for further processing
  *
  * @param twitter
  * @param user
  * @param page
  * @param size
  */
class FetchPage(twitter: Twitter, user: Long, page: Int, size: Int) extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.system.dispatcher

  override def preStart(): Unit = {
    Future {
      val p = new Paging(page, size)
      twitter.getUserTimeline(user, p)
    } pipeTo self
  }
  override def receive: Receive = {
    case xs: ResponseList[Status] =>
      context.parent ! FetchPage.Page(xs.asScala.toList)
    case Failure(t) => throw t
  }
}
