package com.ccleaninc.cclean.security.controller;

import com.ccleaninc.cclean.security.services.Auth0UserManagementService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth0/users")
public class Auth0UsersController {

    private final Auth0UserManagementService userMgmtService;

    public Auth0UsersController(Auth0UserManagementService userMgmtService) {
        this.userMgmtService = userMgmtService;
    }

    // (Optional) GET all users if you want to list them
    @GetMapping
    public ResponseEntity<String> listUsers() {
        String usersJson = userMgmtService.getUsers();
        return ResponseEntity.ok(usersJson);
    }

    // (Optional) GET user permissions if you want them
    @GetMapping("/{userId}/permissions")
    public ResponseEntity<String> getUserPermissions(@PathVariable String userId) {
        String permsJson = userMgmtService.getUserPermissions(userId);
        return ResponseEntity.ok(permsJson);
    }

    // **POST** => assign a role to the user
    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> assignRole(
            @PathVariable String userId,
            @RequestBody RoleAssignmentRequest request
    ) {
        userMgmtService.assignRole(userId, request.getRoleId());
        return ResponseEntity.ok().build();
    }

    // **DELETE** => remove a role from the user
    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<Void> removeRole(
            @PathVariable String userId,
            @RequestBody RoleAssignmentRequest request
    ) {
        userMgmtService.removeRole(userId, request.getRoleId());
        return ResponseEntity.ok().build();
    }

    // Simple DTO: { "roleId": "rol_abc123" }
    @Getter @Setter
    public static class RoleAssignmentRequest {
        private String roleId;
    }
}



