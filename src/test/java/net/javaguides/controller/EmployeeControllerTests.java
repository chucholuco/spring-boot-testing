package net.javaguides.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.model.Employee;
import net.javaguides.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("Jesus")
                .lastName("Tapia")
                .email("chucholuco@gmail.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Jesus").lastName("Tapia").email("tapia0@hotmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Azalia").lastName("Santiago").email("ledtere@hotmail.com").build());

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeesObject() throws Exception {
        //given
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Jesus")
                .lastName("Tapia")
                .email("chucholuco@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Jesus")
                .lastName("Tapia")
                .email("chucholuco@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnEmployeeObject() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Jesus")
                .lastName("Tapia")
                .email("chucholuco@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Azalia")
                .lastName("Santiago")
                .email("ledtere@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturn404() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Jesus")
                .lastName("Tapia")
                .email("chucholuco@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Azalia")
                .lastName("Santiago")
                .email("ledtere@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //given
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then
        response.andExpect(status().isOk()).andDo(print());
    }
}
