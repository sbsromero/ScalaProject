package domain.query

import adapter.EmployeeAdapter
import cats.data.EitherT
import domain.model.Employee
import monix.eval.Task

class getEmployeeByIdQuery(employeeAdapter: EmployeeAdapter) {

  /*def execute(employeeId: String): Future[Option[Employee]] = {
    employeeAdapter.getEmployeeById(employeeId)
  }*/

  def execute(employeeId: String): EitherT[Task, String, Employee] = {
    employeeAdapter.getEmployeeById(employeeId)
  }
}
