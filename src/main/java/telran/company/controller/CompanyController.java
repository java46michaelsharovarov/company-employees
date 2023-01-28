package telran.company.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import telran.company.model.Employee;
import telran.company.model.NewEmployee;
import telran.company.service.CompanyService;

@Validated
@RestController
@RequestMapping("employees")
public class CompanyController {

	public static final String AGE_MESSAGE = "age should be in range [20 - 70]";
	public static final String MONTH_MESSAGE = "month should be in range [1 - 12]";
	
	public static final String REQUEST_TO_GET_BY_SALARY = "request to get employees by salary from {} to {}";
	public static final String REQUEST_TO_GET_BY_AGE = "request to get employees by age from {} to {}";
	public static final String REQUEST_TO_GET_BY_MONTH = "request to get employees by month {}";
	public static final String REQUEST_TO_ADD = "request to add employee : {}";
	public static final String REQUEST_TO_UPDATE = "request to update employee : {}";
	public static final String REQUEST_TO_DELETE = "request to delete employee : {}";
	public static final String RESPONSE_OK = "responce status - OK";
	@Autowired
	CompanyService service;
	@Autowired
	ObjectMapper mapper;
	Logger LOG = LoggerFactory.getLogger(CompanyController.class);

	@PostMapping
	String post(@RequestBody @Valid NewEmployee employee) throws JsonProcessingException {
		LOG.debug(REQUEST_TO_ADD, employee.toString());
		Employee addedEmployee = service.addEmployee(employee);
		LOG.debug(RESPONSE_OK);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(addedEmployee);
	}

	@PutMapping
	String put(@RequestBody @Valid Employee employee) throws JsonProcessingException {
		LOG.debug(REQUEST_TO_UPDATE, employee.toString());
		Employee updatedEmployee = service.updateEmployee(employee);		
		LOG.debug(RESPONSE_OK);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(updatedEmployee);
	}
	
	@DeleteMapping("/{id}")
	String delete(@Min(value = 100_000_000, message = Employee.ID_MESSAGE)
				  @Max(value = 999_999_999, message = Employee.ID_MESSAGE)
				  @NotNull(message = NewEmployee.NULL_MESSAGE)
				  @PathVariable("id") int id) throws JsonProcessingException {
		LOG.debug(REQUEST_TO_DELETE, id);
		Employee deletedEmployee = service.deleteEmployee(id);		
		LOG.debug(RESPONSE_OK);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deletedEmployee); 
	}
	
	@GetMapping("/salary/{salaryFrom}/{salaryTo}")
	String getBySalary(@Min(value = 5_000, message = NewEmployee.SALARY_MESSAGE)
				@Max(value = 45_000, message = NewEmployee.SALARY_MESSAGE)
				@NotNull(message = NewEmployee.NULL_MESSAGE)
				@PathVariable("salaryFrom") int salaryFrom,
				@PathVariable("salaryTo") int salaryTo) throws JsonProcessingException {
		LOG.debug(REQUEST_TO_GET_BY_SALARY, salaryFrom, salaryTo);
		List<Employee> employeesBySalary = service.employeesBySalary(salaryFrom, salaryTo);		
		LOG.debug(RESPONSE_OK);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeesBySalary);
	}
	
	@GetMapping("/age/{ageFrom}/{ageTo}")
	String getByAge(@Min(value = 20, message = AGE_MESSAGE)
				@Max(value = 70, message = AGE_MESSAGE)
				@NotNull(message = NewEmployee.NULL_MESSAGE)
				@PathVariable("ageFrom") int ageFrom,
				@Min(value = 20, message = AGE_MESSAGE)
				@Max(value = 70, message = AGE_MESSAGE)
				@NotNull(message = NewEmployee.NULL_MESSAGE)
				@PathVariable("ageTo") int ageTo) throws JsonProcessingException {
		LOG.debug(REQUEST_TO_GET_BY_AGE, ageFrom, ageTo);
		List<Employee> employeesByAge = service.employeesByAge(ageFrom, ageTo);		
		LOG.debug(RESPONSE_OK);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeesByAge);
	}
	
	@GetMapping("/month/{monthNumber}")
	String getByMonth(@Min(value = 1, message = MONTH_MESSAGE)
				@Max(value = 12, message = MONTH_MESSAGE)
				@NotNull(message = NewEmployee.NULL_MESSAGE)
				@PathVariable("monthNumber") int month) throws JsonProcessingException {
		LOG.debug(REQUEST_TO_GET_BY_MONTH, month);
		List<Employee> employeesByBirthMonth = service.employeesByBirthMonth(month);
		LOG.debug(RESPONSE_OK);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeesByBirthMonth);
	}
	
	@PreDestroy
	void shutdown() {
		LOG.info("bye, performed graceful shutdown");
	}

}
