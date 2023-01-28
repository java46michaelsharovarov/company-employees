package telran.company.accountmanagement.model;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	@Email(message = "Username should be in format of Email")
	public String username;
	
	@NotNull
	@Size(min = 6, message = "Password should not be less than 6 characters")
	public String password;
	
	@NotNull
	@Pattern(regexp = "ADMIN|USER|ACCOUNTER|ADMIN_COMPANY", message = "Role should be one of: {regexp}")
	public String role;
	
}
