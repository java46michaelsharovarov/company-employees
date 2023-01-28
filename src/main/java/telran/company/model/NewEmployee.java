package telran.company.model;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class NewEmployee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String SALARY_MESSAGE = "salary should be in range [5000 - 45000]";
	public static final String NAME_MESSAGE = "first letter of the first and last name must be uppercase";
	public static final String NULL_MESSAGE = "can't be null";

	@NotNull(message = "first name " + NULL_MESSAGE)
	@Pattern(regexp = "[A-Z][a-z]*", message = NAME_MESSAGE)
	private String firstName;
	
	@NotNull(message = "last name " + NULL_MESSAGE)
	@Pattern(regexp = "[A-Z][a-z]*", message = NAME_MESSAGE)
	private String lastName;
	
	@NotNull(message = "birth date " + NULL_MESSAGE)
	@Pattern(regexp = "\\d{4}-(0\\d|1[012])-(0\\d|[12]\\d|3[01])",
			message = "birth date should be in format YYYY-MM-DD")
	private String birthDate;
	
	@Min(value = 5_000, message = SALARY_MESSAGE)
	@Max(value = 45_000, message = SALARY_MESSAGE)
	private int salary; 
	
}
