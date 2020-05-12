package domain.command

import adapter.EmployeeAdapter
import domain.model.Employee
import monix.eval.Task

class saveEmployeeCommand(employeeAdapter: EmployeeAdapter) {
  /*def execute(employee: Employee) = {
    employeeAdapter.saveEmployee(employee)
  }*/
  def execute(employee: Employee): Task[List[Employee]] = {
    employeeAdapter.saveEmployee(employee)
  }
}
