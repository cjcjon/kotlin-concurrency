import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.lang.UnsupportedOperationException

@OptIn(InternalCoroutinesApi::class)
fun main(args: Array<String>) = runBlocking {
  val task = GlobalScope.async {
    doSomething()
  }

  task.join()
  if (task.isCancelled) {
    val exception = task.getCancellationException()
    println("Error with message: ${exception.cause}")
  } else {
    println("Success")
  }

  println("Completed")
}

fun doSomething() {
  throw UnsupportedOperationException("Can't do")
}