import kotlinx.coroutines.*

fun main() = runBlocking {
  val defaultContextTask = launch {
    printCurrentThread()
  }
  defaultContextTask.join()

  val disaptcher = newSingleThreadContext(name = "ServiceCall")
  val contextTask = GlobalScope.launch(disaptcher) {
    printCurrentThread()
  }
  contextTask.join()
}

fun printCurrentThread() {
  println("Running in thread [${Thread.currentThread().name}]")
}