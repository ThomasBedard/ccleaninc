package com.ccleaninc.cclean.security.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String userId;
    private String email;

    public UserResponse(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

}
