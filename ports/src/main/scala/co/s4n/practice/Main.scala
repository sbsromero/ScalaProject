package co.s4n.practice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import co.s4n.practice.httpService.RouteService

object Main extends App {

  val routeService = new RouteService
  val routeMap = routeService.routeMap
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  val bindingFuture = Http().bindAndHandle(routeMap, "localhost", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

}
