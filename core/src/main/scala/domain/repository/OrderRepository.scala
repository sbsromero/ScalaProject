package domain.repository

import cats.data.EitherT
import domain.model.Employee
import monix.eval.Task

class OrderRepository() {

  @SuppressWarnings(Array("org.wartremover.warts.All"))
  var employees: List[Employee] = {
    List(Employee("1", "employee", "test", "test@gmail.com"),
         Employee("2", "test", "test", "test@gmail.com"))
  }

  /*def getEmployeeById(itemId: String): Future[Either[String, Employee]] =
    Future {
      val employee = employees.find(o => o.id == itemId)
      employee match {
        case Some(e) => Right(e)
        case None => Left(s"The employee with the id: $itemId wasn't found in the BD")
      }
    }*/

  def getEmployeeById(itemId: String): EitherT[Task, String, Employee] = {
    EitherT(
      Task.now {
        val employee = employees.find(o => o.id == itemId)
        employee
          .map(Right(_))
          .getOrElse(
            Left(s"The employee with the id: $itemId wasn't found in the BD"))
      }
    )
  }

  def listEmployees(): Task[List[Employee]] = {
    Task.now(employees)
  }

  /*def saveEmployee(employee: Employee): Future[List[Employee]] =
    Future.successful {
      employees = employees :+ employee
      employees
    }*/

  def saveEmployee(employee: Employee): Task[List[Employee]] = {
    Task.now {
      employees = employees :+ employee
      employees
    }
  }
}
