import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, Merge, MergePreferred, RunnableGraph, Sink, Source}

object Main extends App {

  println("Main Started!")

  implicit val system = ActorSystem()
  implicit val materializer =  ActorMaterializer()
  import system.dispatcher

  /*Source.repeat("Test String")
    .zip(Source.fromIterator(() => Iterator.from(0)))
    .take(7)
    .mapConcat{
      case (str, num) =>
        val i =  " " * num
        f"$i$str%n"
    }
    .toMat(Sink.foreach(x => print(x)))(Keep.right)
    .run()
    .onComplete(_ => system.terminate())*/

  RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val source = Source(1 to 30)
    val merge = b.add(Merge[Int](2))
    //val merge = b.add(MergePreferred[Int](1))
    val bcast = b.add(Broadcast[Int](2))

    source ~> merge ~> Flow[Int].map { s => println(s); s } ~> bcast ~> Sink.ignore
              merge                  <~                      bcast

    ClosedShape
  }).run()

}
