import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}

object Main extends App {

  println("Main Started!")

  implicit val system = ActorSystem()
  implicit val materializer =  ActorMaterializer()
  import system.dispatcher

  Source.repeat("Test String")
    .zip(Source.fromIterator(() => Iterator.from(0)))
    .take(7)
    .mapConcat{
      case (str, num) =>
        val i =  " " * num
        f"$i$str%n"
    }
    .toMat(Sink.foreach(x => print(x)))(Keep.right)
    .run()
    .onComplete(_ => system.terminate())

}
