package domain.query

import adapter.EmployeeAdapter
import domain.model.Employee
import monix.eval.Task

class getEmployeeListQuery(employeeAdapter: EmployeeAdapter) {

  def execute(): Task[List[Employee]] = {
    employeeAdapter.getEmployees()
  }
}
