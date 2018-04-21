package com.github.jpringle.ss.ingest

import akka.actor.ActorSystem
import com.github.jpringle.ss.ingest.actor.TweetFetcher
import com.typesafe.config.ConfigFactory
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

object Main extends App {

  val conf = ConfigFactory.load()
  val twitterConf = new ConfigurationBuilder()
    .setOAuthConsumerKey(conf.getString("credentials.twitter.oauth-consumer-key"))
    .setOAuthConsumerSecret(conf.getString("credentials.twitter.oauth-consumer-secret"))
    .setOAuthAccessToken(conf.getString("credentials.twitter.oauth-access-token"))
    .setOAuthAccessTokenSecret(conf.getString("credentials.twitter.oauth-access-secret"))
    .setTweetModeExtended(true)
    .build()

  val donald = 25073877
  val twitter = new TwitterFactory(twitterConf).getInstance()

  val sys = ActorSystem.create("ss")
  val props = TweetFetcher.props(twitter, donald)
  sys.actorOf(props)
}
