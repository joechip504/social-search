package jpringle.fetch.twitter

import scala.collection.JavaConverters._
import com.typesafe.config.ConfigFactory
import twitter4j.{ResponseList, Status, TwitterFactory}
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
  val r: ResponseList[Status] = twitter.getUserTimeline(donald)

  println(s"RateLimitStatus: ${r.getRateLimitStatus}")

  r.iterator().asScala.foreach { s =>
    println(s"Created: ${s.getCreatedAt}")
    println(s"Text: ${s.getText}")
    println
  }

}
