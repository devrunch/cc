package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee != null) {
            employee.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee);
        }
    }

    public List<Employee> findEmployeesForService(DayOfWeek day, Set<EmployeeSkill> skills) {
        // 1. Ask the database: "Who is free on this day?"
        List<Employee> employeesOnDay = employeeRepository.findAllByDaysAvailable(day);

        // 2. Filter the list: "Who also has the skills we need?"
        List<Employee> matchingEmployees = new ArrayList<>();
        for (Employee employee : employeesOnDay) {
            if (employee.getSkills().containsAll(skills)) {
                matchingEmployees.add(employee);
            }
        }
        return matchingEmployees;
    }

    // Helper to find owner by pet (Rubric requirement)
    public Customer getOwnerByPet(Long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet != null) {
            return pet.getCustomer();
        }
        return null;
    }
}