import kotlinx.coroutines.*
import rss.RssReader
import javax.xml.parsers.DocumentBuilderFactory

fun main(): Unit = runBlocking {
  fun asyncLoadNews(feeds: List<String>, rssReader: RssReader, dispatcher: CoroutineDispatcher) = GlobalScope.launch {
    val requests = feeds.map { rssReader.asyncFetchHeadlines(it, dispatcher) }
    requests.forEach { it.join() }

    val headlines = requests
      .filter { !it.isCancelled }
      .flatMap { it.getCompleted() }

    val failed = requests.filter { it.isCancelled }.size

    println("Success: ${feeds.size - failed}")
    println("Failed: ${failed}")
    println((0 until 1).map { System.lineSeparator() }.joinToString(""))
    println(headlines.joinToString(System.lineSeparator()))
  }

  val feeds = listOf(
    "https://feeds.npr.org/1001/rss.xml",
    "http://rss.cnn.com/rss/cnn_topstories.rss",
    "http://feeds.foxnews.com/foxnews/politics?format=xml",
  )
  val dispatcher = newFixedThreadPoolContext(2, "IO")
  val rssReader = RssReader(DocumentBuilderFactory.newInstance())

  asyncLoadNews(feeds, rssReader, dispatcher).join()
}