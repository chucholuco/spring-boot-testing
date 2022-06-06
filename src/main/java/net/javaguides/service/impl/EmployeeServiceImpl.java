package net.javaguides.service.impl;

import net.javaguides.exception.ResourceNotFoundException;
import net.javaguides.model.Employee;
import net.javaguides.repository.EmployeeRepository;
import net.javaguides.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with email: " + employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {

        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updateEmployee) {
        return employeeRepository.save(updateEmployee);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
}
