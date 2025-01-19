package com.ccleaninc.cclean.security.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse validateUser(String email, String password) throws InvalidCredentialsException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!BCrypt.checkpw(password, user.getPassword_hash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return new UserResponse(user.getId(), user.getEmail());
    }
}

