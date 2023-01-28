package telran.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.company.model.Employee;
import telran.company.model.NewEmployee;
import telran.company.service.CompanyService;


interface TestConstants {
	String FILE_NAME = "test-employyes.data";
}

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties = {"app.employees.data.fileName =" + TestConstants.FILE_NAME})
class CompanyApplicationServiceTests {

	private static final int SALARY1 = 12000;
	private static final int SALARY2 = 20000;
	private static final int SALARY3 = 30000;
	private static final String BIRTH_DATE1 = "2000-11-01"; // 22
	private static final String BIRTH_DATE2 = "1995-11-01"; // 27
	private static final String BIRTH_DATE3 = "1990-10-01"; // 32
	private static final String LAST_NAME = "Ivanov";
	private static final String NAME = "Victor";
	private static final int ID1 = 100000001;
	private static final int ID2 = 999999999;
	private static NewEmployee employee1 = new NewEmployee(NAME, LAST_NAME, BIRTH_DATE1, SALARY1);
	private static NewEmployee employee2 = new NewEmployee(NAME, LAST_NAME, BIRTH_DATE2, SALARY2);
	private static NewEmployee employee3 = new NewEmployee(NAME, LAST_NAME, BIRTH_DATE3, SALARY3);
	private static Employee expected = new Employee(ID1, NAME, LAST_NAME, BIRTH_DATE1, SALARY1);
	private static Employee noExistEmployee = new Employee(ID2, NAME, LAST_NAME, BIRTH_DATE1, SALARY1);
	private static  Employee addedEmployee1;
	private static  Employee addedEmployee2;
	private static  Employee addedEmployee3;
	private static  Logger LOG = LoggerFactory.getLogger(CompanyApplicationServiceTests.class);
	@Autowired
	CompanyService companyService;

	@BeforeAll
	static void setUpBeforeAll() throws IOException {
		if (Files.deleteIfExists(Path.of(TestConstants.FILE_NAME))) {
			LOG.info("file {} has been deleted", TestConstants.FILE_NAME);
		} else {
			LOG.info("file {} not found", TestConstants.FILE_NAME);
		}
	}

	@Test
	@Order(1)
	void addEmployee() {
		NewEmployee employee = new NewEmployee(NAME, LAST_NAME, BIRTH_DATE1, SALARY1);
		assertEquals(expected, companyService.addEmployee(employee));
	}

	@Test
	@Order(2)
	void updateEmployee() {
		Employee employee = new Employee(ID1, NAME, LAST_NAME, BIRTH_DATE1, SALARY2);
		assertEquals(expected, companyService.updateEmployee(employee));
		assertThrows(NoSuchElementException.class, () -> companyService.updateEmployee(noExistEmployee));
	}
	
	@Test
	@Order(3)
	void deleteEmployee() {
		Employee employee = new Employee(ID1, NAME, LAST_NAME, BIRTH_DATE1, SALARY2);
		assertEquals(employee, companyService.deleteEmployee(ID1));
		assertThrows(NoSuchElementException.class, () -> companyService.deleteEmployee(ID2));
	}
	
	@Test
	@Order(4)
	void getEmployeeBySalary() {
		List<Employee> expectedList = new ArrayList<>();
		addedEmployee1 = companyService.addEmployee(employee1);
		expectedList.add(addedEmployee1);
		addedEmployee2 = companyService.addEmployee(employee2);
		expectedList.add(addedEmployee2);
		addedEmployee3 = companyService.addEmployee(employee3);
		assertEquals(expectedList, companyService.employeesBySalary(SALARY1, SALARY2));
		assertThrows(IllegalArgumentException.class, () -> companyService.employeesBySalary(SALARY2, SALARY1));
	}
	
	@Test
	@Order(5) 
	void getEmployeeByAge() {
		List<Employee> expectedList = new ArrayList<>();
		expectedList.add(addedEmployee1);
		expectedList.add(addedEmployee2);
		assertEquals(expectedList, companyService.employeesByAge(20, 30));
		assertThrows(IllegalArgumentException.class, () -> companyService.employeesByAge(30, 20));
	}
	
	@Test
	@Order(6)
	void getEmployeeByMonth() {
		List<Employee> expectedList = new ArrayList<>();
		expectedList.add(addedEmployee3);
		assertEquals(expectedList, companyService.employeesByBirthMonth(10));
	}

}
