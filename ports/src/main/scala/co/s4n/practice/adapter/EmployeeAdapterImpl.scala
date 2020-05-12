package co.s4n.practice.adapter

import adapter.EmployeeAdapter
import akka.actor.ActorSystem
import cats.data.EitherT
import domain.model.Employee
import domain.repository.OrderRepository
import monix.eval.Task

class EmployeeAdapterImpl extends EmployeeAdapter {
  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  val orderRepository = new OrderRepository()

  override def getEmployees(): Task[List[Employee]] = {
    orderRepository.listEmployees()
  }

  /*override def getEmployeeById(employeeId: String): Future[Option[Employee]] = {
    orderRepository.getEmployeeById(employeeId)
  }*/
  override def getEmployeeById(employeeId: String): EitherT[Task, String, Employee] = {
    /*EitherT(
      Task.fromFuture(orderRepository.getEmployeeById(employeeId))
      // Task.now(EitherT.fromEither(orderRepository.getEmployeeById(employeeId)))
    )*/
    orderRepository.getEmployeeById(employeeId)
  }

  /*override def saveEmployee(employee: Employee): Future[List[Employee]] = {
    orderRepository.saveEmployee(employee)
  }*/

  override def saveEmployee(employee: Employee): Task[List[Employee]] = {
    orderRepository.saveEmployee(employee)
  }
}
