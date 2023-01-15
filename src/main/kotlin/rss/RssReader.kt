package rss

import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

class RssReader(val factory: DocumentBuilderFactory) {

  fun fetchRssHeadlines(url: String): List<String> {
    val builder = factory.newDocumentBuilder()
    val xml = builder.parse(url)
    val news = xml.getElementsByTagName("channel").item(0)

    return (0 until news.childNodes.length)
      .map { news.childNodes.item(it) }
      .filter { Node.ELEMENT_NODE == it.nodeType }
      .map { it as Element }
      .filter { "item" == it.tagName }
      .map { it.getElementsByTagName("title").item(0).textContent }
  }


}