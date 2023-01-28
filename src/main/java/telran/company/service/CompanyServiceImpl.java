package telran.company.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import telran.company.controller.CompanyController;
import telran.company.model.Employee;
import telran.company.model.NewEmployee;

@Service
public class CompanyServiceImpl implements CompanyService {

	HashMap<Integer, Employee> employees;
	TreeMap<Integer, List<Employee>> employeesBySalary = new TreeMap<>();
	TreeMap<Integer, List<Employee>> employeesByAge = new TreeMap<>();
	HashMap<Integer, List<Employee>> employeesByBirthMonth = new HashMap<>();
	int id = 100_000_000;
	@Value("${app.employees.data.fileName}")
	String fileName;

	@Override
	public Employee addEmployee(NewEmployee employee) {
		while (employees.containsKey(++id)) {
			LOG.debug("id {} already exist", id);
		}
		Employee employeeRes = new Employee(id, employee.getFirstName(), employee.getLastName(),
				employee.getBirthDate(), employee.getSalary());
		addEmployeeToLists(employeeRes);
		employees.put(id, employeeRes);
		LOG.debug("employee has been added: {}", employeeRes.toString());
		return employeeRes;
	}

	@Override
	public Employee updateEmployee(Employee employee) { 
		Employee employeeRes = employees.get(employee.getId());
		if (employeeRes == null) {
			LOG.error("employee {} doesn't exist", employee.getId()); 
			throw new NoSuchElementException();
		}
		removeEmployeeFromLists(employeeRes);
		addEmployeeToLists(employee);
		employees.put(employee.getId(), employee);
		LOG.debug("employee {} has been updated", employee.getId());
		return employeeRes;
	} 

	@Override
	public Employee deleteEmployee(int id) {
		Employee employeeRes = employees.get(id);
		if (employeeRes == null) {
			LOG.error("employee {} doesn't exist", id);
			throw new NoSuchElementException();
		}
		removeEmployeeFromLists(employeeRes);
		employees.remove(id);
		LOG.debug("employee {} has been deleted", id);
		return employeeRes;
	}

	@Override
	public List<Employee> employeesBySalary(int salaryFrom, int salaryTo) {
		if(salaryFrom > salaryTo) {
			LOG.error("initial value of salary ({}) is greater than final value ({})", salaryFrom, salaryTo);
			throw new IllegalArgumentException(String.format("initial value of salary (%d) is greater than final value (%d)", salaryFrom, salaryTo));
		}
		List<Employee> listBySalary = employeesBySalary.subMap(salaryFrom, true, salaryTo, true).values().stream().flatMap(e -> e.stream()).toList();
		if (listBySalary == null) {
			LOG.warn("list employeesBySalary is null");
			return Collections.emptyList();
		}		
		LOG.debug("employees by salary from {} to {}: {}", salaryFrom, salaryTo, listBySalary);
		return listBySalary;
	}

	@Override
	public List<Employee> employeesByAge(int ageFrom, int ageTo) {
		if(ageFrom > ageTo) {
			LOG.error("initial value of age ({}) is greater than final value ({})", ageFrom, ageTo);
			throw new IllegalArgumentException(String.format("initial value of age (%d) is greater than final value (%d)", ageFrom, ageTo));
		}
		List<Employee> listByAge = employeesByAge.subMap(ageFrom, true, ageTo, true).values().stream().flatMap(e -> e.stream()).toList();
		if (listByAge == null) {
			LOG.warn("list employeesByAge is null");
		}
		LOG.debug("employees by age from {} to {}: {}", ageFrom, ageTo, listByAge);
		return listByAge;
	}

	@Override
	public List<Employee> employeesByBirthMonth(int monthNumber) {
		List<Employee> listByMonth = employeesByBirthMonth.get(monthNumber);
		if(listByMonth == null) {
			LOG.warn("list employeesByBirthMonth is null");
			return Collections.emptyList();
		}
		LOG.debug("employees by month {}: {}", monthNumber, listByMonth);
		return listByMonth;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	void restoreEmployees() {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
			employees = (HashMap<Integer, Employee>) input.readObject();
			employees.forEach((id, empl) -> addEmployeeToLists(empl));
			LOG.info("employees restored from file {}", fileName);
		} catch (FileNotFoundException e) {
			LOG.warn("file {} doesn't exists", fileName);
			employees = new HashMap<>();
		} catch (Exception e) {
			LOG.error("error at restoring employees {}", e);
		}
	}

	@PreDestroy
	void save() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
			output.writeObject(employees);
			LOG.info("data is saved to file - {}", fileName);
		} catch (IOException e) {
			LOG.error("saving to file caused exception {}", e.getMessage());
		}
	}

	private void addEmployeeToLists(Employee empl) {
		LocalDate birthDate = LocalDate.parse(empl.getBirthDate());
		Integer age = Period.between(birthDate, LocalDate.now()).getYears();
		if(age < 20 || age > 70) {
			LOG.error("{}; current: {}", CompanyController.AGE_MESSAGE, age);
			throw new IllegalArgumentException(String.format("%s; current: %d", CompanyController.AGE_MESSAGE, age));
		}
		employeesBySalary.computeIfAbsent(empl.getSalary(), ArrayList::new).add(empl);
		employeesByAge.computeIfAbsent(age, ArrayList::new).add(empl);
		employeesByBirthMonth.computeIfAbsent(birthDate.getMonthValue(), ArrayList::new).add(empl);
	}

	private void removeEmployeeFromLists(Employee empl) {
		LocalDate birthDate = LocalDate.parse(empl.getBirthDate());
		Integer age = Period.between(birthDate, LocalDate.now()).getYears();
		employeesBySalary.remove(empl.getSalary());
		employeesByAge.remove(age);
		employeesByBirthMonth.remove(birthDate.getMonthValue());
	}

}
