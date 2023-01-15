import kotlinx.coroutines.*
import rss.RssReader
import rss.model.Feed
import javax.xml.parsers.DocumentBuilderFactory

fun main(): Unit = runBlocking {
  fun asyncLoadNews(feeds: List<Feed>, rssReader: RssReader, dispatcher: CoroutineDispatcher) = GlobalScope.launch {
    val requests = feeds.map { rssReader.asyncFetchArticles(it, dispatcher) }
    requests.forEach { it.join() }

    val articles = requests
      .filter { !it.isCancelled }
      .flatMap { it.getCompleted() }

    val failed = requests.filter { it.isCancelled }.size

    println("Success: ${feeds.size - failed}")
    println("Failed: ${failed}")
    println((0 until 1).map { System.lineSeparator() }.joinToString(""))

    println(articles.map { it.title }.joinToString(System.lineSeparator()))
  }

  val feeds = listOf(
    Feed("npr", "https://feeds.npr.org/1001/rss.xml"),
    Feed("cnn", "http://rss.cnn.com/rss/cnn_topstories.rss"),
    Feed("fox", "http://feeds.foxnews.com/foxnews/politics?format=xml"),
    Feed("inv", "htt:myNewsFeed")
  )
  val dispatcher = newFixedThreadPoolContext(2, "IO")
  val rssReader = RssReader(DocumentBuilderFactory.newInstance())

  asyncLoadNews(feeds, rssReader, dispatcher).join()
}