package net.javaguides.service;

import net.javaguides.exception.ResourceNotFoundException;
import net.javaguides.model.Employee;
import net.javaguides.repository.EmployeeRepository;
import net.javaguides.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Jesus")
                .lastName("Tapia")
                .email("chucholuco@gmail.com")
                .build();
    }

    //JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given

        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);
        System.out.println(employeeRepository);
        System.out.println(employeeService);

        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);

        //then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for saveEmployee method which throws Exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenReturnThrowsException() {
        //given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for getAllEmployees")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesLists() {
        //given
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Azalia")
                .lastName("Santiago")
                .email("ledtere@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getAllEmployees given Empty List")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        //given
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Azalia")
                .lastName("Santiago")
                .email("ledtere@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.EMPTY_LIST);

        //when
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for get Employee Object by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for update Employee object")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("test@test.com");
        employee.setFirstName("test");

        //when
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then
        assertThat(updatedEmployee.getEmail()).isEqualTo("test@test.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("test");

    }

    @DisplayName("JUnit test for delete Employee object")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        long employeeId = 1L;

        //given
        willDoNothing().given(employeeRepository).deleteById(1L);
        //when
        employeeService.deleteEmployee(employeeId);

        //then
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }
}
