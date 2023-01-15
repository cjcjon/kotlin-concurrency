import kotlinx.coroutines.*
import java.lang.UnsupportedOperationException

@OptIn(InternalCoroutinesApi::class)
fun main(args: Array<String>) = runBlocking {
  val task = GlobalScope.launch {
    doSomething()
  }
  task.join()

  println("Completed")
}

fun doSomething() {
  throw UnsupportedOperationException("Can't do")
}