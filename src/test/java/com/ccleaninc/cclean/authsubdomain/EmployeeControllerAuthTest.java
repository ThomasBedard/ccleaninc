package com.ccleaninc.cclean.authsubdomain;

//import com.ccleaninc.cclean.config.SecurityConfig;
//import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
//import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeController;
//import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.http.RequestEntity.get;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = EmployeeController.class)
//@Import(SecurityConfig.class)
//@AutoConfigureMockMvc(addFilters = true)
public class EmployeeControllerAuthTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean // or @TestDouble if available
//    private EmployeeService employeeService;
//
//    @Test
//    void whenUserHasAdminPermission_thenCanAccessEmployees() throws Exception {
//        // Mock service to return a non-empty list
//        when(employeeService.getAllEmployees()).thenReturn(List.of(new EmployeeResponseModel()));
//
//        mockMvc.perform(get("/api/v1/employees")
//                        .with(jwt().jwt(jwt -> jwt.claim("permissions", List.of("admin")))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void whenUserHasNoAdminPermission_thenForbidden() throws Exception {
//        // This test should fail with 403 before it hits the controller logic
//        // The employees return is irrelevant because we expect security to block
//        when(employeeService.getAllEmployees()).thenReturn(List.of(new EmployeeResponseModel()));
//
//        mockMvc.perform(get("/api/v1/employees")
//                        .with(jwt().jwt(jwt -> jwt.claim("permissions", List.of()))))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void whenNoTokenProvided_thenUnauthorized() throws Exception {
//        // No token, so should be blocked by security
//        when(employeeService.getAllEmployees()).thenReturn(List.of(new EmployeeResponseModel()));
//
//        mockMvc.perform(get("/api/v1/employees"))
//                .andExpect(status().isUnauthorized());
//    }
}




