package com.ccleaninc.cclean.security.presentationlayer;

import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeController;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import com.ccleaninc.cclean.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
class EmployeeControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public JwtDecoder mockJwtDecoder() {
            return Mockito.mock(JwtDecoder.class); // Mock JwtDecoder for the test context
        }

        @Bean
        @Primary
        public EmployeeService mockEmployeeService() {
            return Mockito.mock(EmployeeService.class); // Mock EmployeeService for the test context
        }
    }

    @BeforeEach
    void setup() {
        List<EmployeeResponseModel> mockEmployees = List.of(
                new EmployeeResponseModel("1", "John", "Doe", "john.doe@example.com", "123456789", "Admin", true),
                new EmployeeResponseModel("2", "Jane", "Smith", "jane.smith@example.com", "987654321", "Employee", true)
        );
        Mockito.when(employeeService.getAllEmployees()).thenReturn(mockEmployees);
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"admin"})
    void shouldGetEmployeeByIdWhenAdmin() throws Exception {
        Mockito.when(employeeService.getEmployeeByEmployeeId("1"))
                .thenReturn(new EmployeeResponseModel("1", "John", "Doe", "john.doe@example.com", "123456789", "Admin", true));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }


    @Test
    void shouldReturnUnauthorizedWhenNoUserIsAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isUnauthorized());
    }

}
