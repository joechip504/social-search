package jpringle.fetch.twitter

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
    .build()

  val donald = 25073877
  val twitter = new TwitterFactory(twitterConf).getInstance()
  val r: ResponseList[Status] = twitter.getUserTimeline(donald)

  println(s"RateLimitStatus: ${r.getRateLimitStatus}")
  r.iterator().forEachRemaining { s => println(s.getText) }

}
