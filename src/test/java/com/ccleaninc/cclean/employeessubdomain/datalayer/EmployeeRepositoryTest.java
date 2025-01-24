// package com.ccleaninc.cclean.employeessubdomain.datalayer;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest
// @ExtendWith(SpringExtension.class)
// @ActiveProfiles("test")
// public class EmployeeRepositoryTest {

//     @Autowired
//     private EmployeeRepository employeeRepository;

//     private Employee employee1;
//     private Employee employee2;

//     @BeforeEach
//     void setUp() {
//         // Prepare sample employees
//         employee1 = Employee.builder()
//                 .employeeIdentifier(new EmployeeIdentifier("emp-001"))
//                 .firstName("Alice")
//                 .lastName("Johnson")
//                 .email("alice.johnson@example.com")
//                 .phoneNumber("123-456-7890")
//                 .isActive(true)
//                 .role("cleaner")
//                 .build();

//         employee2 = Employee.builder()
//                 .employeeIdentifier(new EmployeeIdentifier("emp-002"))
//                 .firstName("Bob")
//                 .lastName("Williams")
//                 .email("bob.williams@example.com")
//                 .phoneNumber("456-789-1230")
//                 .isActive(false)
//                 .role("supervisor")
//                 .build();

//         employeeRepository.save(employee1);
//         employeeRepository.save(employee2);
//     }

//     @Test
//     void findByEmployeeIdentifier_ShouldReturnEmployee() {
//         Optional<Employee> result = employeeRepository.findByEmployeeIdentifier_EmployeeId("emp-001");
//         assertTrue(result.isPresent());
//         assertEquals("Alice", result.get().getFirstName());
//     }

//     @Test
//     void findByEmployeeIdentifier_ShouldReturnEmptyIfNotFound() {
//         Optional<Employee> result = employeeRepository.findByEmployeeIdentifier_EmployeeId("emp-999");
//         assertFalse(result.isPresent());
//     }

//     @Test
//     void findByEmail_ShouldReturnEmployee() {
//         Employee result = employeeRepository.findByEmail("alice.johnson@example.com");
//         assertNotNull(result);
//         assertEquals("Alice", result.getFirstName());
//     }

//     @Test
//     void findByPhoneNumberContaining_ShouldReturnEmployeesWithMatchingNumbers() {
//         List<Employee> results = employeeRepository.findByPhoneNumberContaining("123");
//         assertEquals(2, results.size()); // Both employees match
//     }

//     @Test
//     void findByPhoneNumberContaining_ShouldReturnEmptyIfNoMatches() {
//         List<Employee> results = employeeRepository.findByPhoneNumberContaining("999");
//         assertTrue(results.isEmpty());
//     }

//     @Test
//     void searchByNameOrEmail_ShouldReturnMatchingEmployees() {
//         List<Employee> results = employeeRepository.searchByNameOrEmail("Alice");
//         assertEquals(1, results.size());
//         assertEquals("Alice", results.get(0).getFirstName());
//     }

//     @Test
//     void searchByNameOrEmail_ShouldIgnoreCaseAndReturnMatchingEmployees() {
//         List<Employee> results = employeeRepository.searchByNameOrEmail("johnson");
//         assertEquals(1, results.size());
//         assertEquals("Alice", results.get(0).getFirstName());
//     }

//     @Test
//     void searchByNameOrEmail_ShouldReturnEmptyIfNoMatches() {
//         List<Employee> results = employeeRepository.searchByNameOrEmail("NonExistent");
//         assertTrue(results.isEmpty());
//     }

//     @Test
//     void findAll_ShouldReturnAllEmployees() {
//         List<Employee> employees = employeeRepository.findAll();
//         assertEquals(2, employees.size());
//     }

//     @Test
//     void deleteById_ShouldRemoveEmployee() {
//         Optional<Employee> employee = employeeRepository.findByEmployeeIdentifier_EmployeeId("emp-001");
//         assertTrue(employee.isPresent());

//         employeeRepository.deleteById(employee.get().getId());

//         Optional<Employee> deletedEmployee = employeeRepository.findByEmployeeIdentifier_EmployeeId("emp-001");
//         assertFalse(deletedEmployee.isPresent());
//     }
// }
