package telran.company.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import telran.company.model.Employee;
import telran.company.model.NewEmployee;

public interface CompanyService {

	Logger LOG = LoggerFactory.getLogger(CompanyService.class);
	Employee addEmployee(NewEmployee employee);
	Employee updateEmployee(Employee employee);
	Employee deleteEmployee(int id);
	List<Employee> employeesBySalary(int salaryFrom, int salaryTo);
	List<Employee> employeesByAge(int salaryFrom, int salaryTo);
	List<Employee> employeesByBirthMonth(int monthNumber);
	
}
