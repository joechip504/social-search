package jpringle.fetch.twitter

import scala.collection.JavaConverters._
import com.typesafe.config.ConfigFactory
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

object Main extends App {

  def printAllTweets(user: Long, twitter: Twitter) = {
    def paginate(paging: Paging): Unit = {
      println(paging.getPage)
      val r = twitter.getUserTimeline(donald, paging)
      r.iterator().asScala.foreach { s =>
        println(s"Created: ${s.getCreatedAt}")
        println(s"Text: ${s.getText}")
        println
      }
      if (r.size() != 0) {
        val nextPage = new Paging(paging.getPage + 1, 100)
        paginate(nextPage)
      }
    }

    val paging = new Paging(1, 100)
    paginate(paging)
  }

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

  printAllTweets(donald, twitter)

}
