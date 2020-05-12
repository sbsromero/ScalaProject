package adapter

import cats.data.EitherT
import domain.model.Employee
import monix.eval.Task

trait EmployeeAdapter {
  def getEmployees(): Task[List[Employee]]

  //def getEmployeeById(employeeId: String): Future[Option[Employee]]
  def getEmployeeById(employeeId: String): EitherT[Task, String, Employee]

  //def saveEmployee(employee: Employee): Future[List[Employee]]
  def saveEmployee(employee: Employee): Task[List[Employee]]
}
