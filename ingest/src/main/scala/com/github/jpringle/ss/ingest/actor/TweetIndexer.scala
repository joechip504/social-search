package com.github.jpringle.ss.ingest.actor

import akka.actor.Actor
import org.apache.lucene.document._
import org.apache.lucene.index.IndexWriter

object TweetIndexer {
}

class TweetIndexer(writer: IndexWriter) extends Actor {

  override val receive: Receive = {
    case TweetFetcher.Tweet(handle, created, content) =>
      val doc = new Document()
      doc.add(new StringField("handle", handle, Field.Store.YES))
      doc.add(new TextField("content", content, Field.Store.YES))
      doc.add(new LongPoint("created", created.getTime))
      writer.addDocument(doc)
      writer.commit()
  }

  override def preStart(): Unit = {
    require(writer.isOpen)
  }

  override def postStop(): Unit = {
    writer.close()
  }

}
