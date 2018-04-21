package com.github.jpringle.ss.ingest.actor

import java.util.Date

import akka.actor.SupervisorStrategy._
import akka.actor._
import twitter4j._

import scala.concurrent.duration._


object TweetFetcher {
  case class Tweet(handle: String, created: Date, content: String)

  def props(twitter: Twitter, user: Long): Props = {
    Props(new TweetFetcher(twitter, user))
  }
}

/**
  * Fetch all tweets for a user.
  *
  * @param twitter
  * @param user
  */
class TweetFetcher(twitter: Twitter, user: Long) extends Actor with ActorLogging {
  private var page = 1

  private def paginate() = {
    val props = FetchPage.props(twitter, user, page)
    context.actorOf(props)
  }

  override def preStart(): Unit = {
    paginate()
  }

  override def receive: Receive = {
    case FetchPage.Page(xs) =>
      if (xs.nonEmpty) {
        page += 1
        paginate()
      }
      xs.foreach { s =>
        val handle = s.getUser.getScreenName
        context.parent ! TweetFetcher.Tweet(handle, s.getCreatedAt, s.getText)
      }
  }
}

