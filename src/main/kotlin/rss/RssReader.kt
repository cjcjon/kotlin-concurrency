package rss

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.w3c.dom.Element
import org.w3c.dom.Node
import rss.model.Article
import rss.model.Feed
import javax.xml.parsers.DocumentBuilderFactory

class RssReader(val factory: DocumentBuilderFactory) {

  fun asyncFetchArticles(feed: Feed, dispatcher: CoroutineDispatcher) = GlobalScope.async(dispatcher) {
    fun getFirstTagContent(element: Element, tag: String) =
      element.getElementsByTagName(tag).item(0).textContent

    val builder = factory.newDocumentBuilder()
    val xml = builder.parse(feed.url)
    val news = xml.getElementsByTagName("channel").item(0)

    (0 until news.childNodes.length)
      .map { news.childNodes.item(it) }
      .filter { Node.ELEMENT_NODE == it.nodeType }
      .map { it as Element }
      .filter { "item" == it.tagName }
      .map {
        val title = getFirstTagContent(it, "title")
        val summary = getFirstTagContent(it, "description")

        Article(feed.name, title, summary)
      }
  }
}