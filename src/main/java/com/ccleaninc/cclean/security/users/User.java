package com.ccleaninc.cclean.security.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String email;
    private String password_hash;

}
