package co.s4n.practice.httpService

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import co.s4n.practice.adapter.EmployeeAdapterImpl
import co.s4n.practice.application.env.Logger
import domain.model.Employee
import domain.query.{getEmployeeByIdQuery, getEmployeeListQuery}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import domain.command.saveEmployeeCommand
import monix.execution.Scheduler.Implicits.global
import io.circe.generic.auto._
import io.circe.syntax._
import scala.util.{Failure, Success}

class RouteService extends Logger {

  val employeeAdapterImpl = new EmployeeAdapterImpl

  val routeMap: Route = concat {
    concat(
      get {
        path("employees") {
          val query = new getEmployeeListQuery(employeeAdapterImpl)
          val employeesTask = query.execute()
          val result = employeesTask.runToFuture
          onComplete(result) {
            case Success(employees) => {
              complete(
                HttpEntity(ContentTypes.`application/json`,
                           employees.asJson.noSpaces))
            }
            case Failure(e) => failWith(e)
          }
        }
      },
      get {
        path("employees" / LongNumber) {
          id =>
            val query = new getEmployeeByIdQuery(employeeAdapterImpl)
            val employeeEitherT = query.execute(id.toString)
            val result = employeeEitherT.value
            onComplete(result.runToFuture) {
              case Success(employee) => {
                complete(
                  HttpEntity(ContentTypes.`application/json`,
                             employee.asJson.noSpaces))
              }
              case Failure(exception) => failWith(exception)
            }
        }
      },
      post {
        path("employees") {
          entity(as[Employee]) {
            request =>
              val command = new saveEmployeeCommand(employeeAdapterImpl)
              val commandTask = command.execute(request)
              val result = commandTask.runToFuture
              onComplete(result) {
                case Success(employees) => {
                  complete(HttpEntity(ContentTypes.`application/json`,
                                      employees.asJson.toString()))
                }
                case Failure(exception) => failWith(exception)
              }
          }
        }
      }
    )

  }
}
