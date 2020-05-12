package domain.model

case class Employee(id: String,
                    firstName: String,
                    lastName: String,
                    email: String)

object Employee {
  def apply(id: String,
            firstName: String,
            lastName: String,
            email: String): Employee =
    new Employee(id, firstName, lastName, email)
}
