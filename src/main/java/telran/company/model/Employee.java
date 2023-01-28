package telran.company.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends NewEmployee {

	private static final long serialVersionUID = 1L;
	public static final String ID_MESSAGE = "id must contain 9 digits";

	public Employee(int id, String firstName, String lastName, String birthDate, int salary) {
		super(firstName, lastName, birthDate, salary);
		this.id = id;
	}

	@Min(value = 100_000_000, message = ID_MESSAGE)
	@Max(value = 999_999_999, message = ID_MESSAGE)
	private int id;
	
	@Override
	public String toString( ) {
		return String.format("id = %d, firstName = %s, lastName = %s, birthDate = %s, salary = %d", id, this.getFirstName(),
				this.getLastName(), this.getBirthDate(), this.getSalary());
	}
	
}
