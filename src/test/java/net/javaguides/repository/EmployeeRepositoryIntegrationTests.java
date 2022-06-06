package net.javaguides.repository;

import net.javaguides.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIntegrationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;



    @BeforeEach
    public void setup() {
         employee = Employee.builder()
                .firstName("Jesus")
                .lastName("Tapia")
                .email("tapia0@hotamil.com")
                .build();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSaveEmployee() {

        //when - action or behaviour that we are going to test
        Employee saveEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(saveEmployee).isNotNull();
        assertThat(saveEmployee.getId()).isGreaterThan(0);

    }

    //JUnit test for find all employees operation
    @DisplayName("JUnit test for find all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        //given
        Employee employee1 = Employee.builder()
                .firstName("Azalia")
                .lastName("Santiago")
                .email("ledtere@gmail.com")
                .build();

        employeeRepository.saveAll(List.of(employee, employee1));

        //when
        List<Employee> employees = employeeRepository.findAll();

        //then
        assertThat(employees).isNotNull();
        //assertThat(employees.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);

        //when
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        //then
        assertThat(employeeDB).isNotNull();

    }

    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);

        //when
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        //then
        assertThat(employeeDB).isNotNull();

    }

    @DisplayName("JUnit test for get update Employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given
        employeeRepository.save(employee);

        //when
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("Michael");
        savedEmployee.setEmail("test@update.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("test@update.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Michael");

    }

    @DisplayName("JUnit test to delete Employee Object")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        //given
        employeeRepository.save(employee);

        //when
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit test for custom query using JPQL index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);
        String firsName = "Jesus";
        String lastName = "Tapia";

        //when
        List<Employee> employeeByJPQL = employeeRepository.findByJPQL(firsName, lastName);

        //then
        assertThat(employeeByJPQL).isNotNull();
    }

    @DisplayName("JUnit test for custom query using JPQL named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);
        String firsName = "Jesus";
        String lastName = "Tapia";

        //when
        List<Employee> employeeByJPQL = employeeRepository.findByJPQLNamedParams(firsName, lastName);

        //then
        assertThat(employeeByJPQL).isNotNull();
    }

    @DisplayName("JUnit test for custom query using Native Query with index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);
        String firsName = "Jesus";
        String lastName = "Tapia";

        //when
        List<Employee> employeeByNativeSQL = employeeRepository.findByNativeSQL(firsName, lastName);

        //then
        assertThat(employeeByNativeSQL).isNotNull();
    }

    @DisplayName("JUnit test for custom query using Native Query with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);
        String firsName = "Jesus";
        String lastName = "Tapia";

        //when
        List<Employee> employeeByNativeSQL = employeeRepository.findByNativeSQLNamedParams(firsName, lastName);

        //then
        assertThat(employeeByNativeSQL).isNotNull();
    }
}
