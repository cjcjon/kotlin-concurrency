import kotlinx.coroutines.*
import rss.RssReader
import javax.xml.parsers.DocumentBuilderFactory

@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking {
  val dispatcher = newSingleThreadContext(name = "ServiceCall")
  val factory = DocumentBuilderFactory.newInstance()
  val rssReader = RssReader(factory)

  val task = GlobalScope.launch(dispatcher) {
    val headLines = rssReader.fetchRssHeadlines("https://feeds.npr.org/1001/rss.xml")
    val compatedHeadLines = headLines.joinToString(System.lineSeparator())
    println(compatedHeadLines)
  }

  task.join()
}