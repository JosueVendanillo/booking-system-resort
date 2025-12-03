package com.bluebell.project.service;

import com.bluebell.project.dto.RegisterRequest;
import com.bluebell.project.model.User;
import com.bluebell.project.model.UserRole;
import com.bluebell.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return BCrypt.checkpw(password, user.getPasswordHash());

        }
        return false;
    }

    public void register(RegisterRequest request) {
        String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        // Determine role
        UserRole role;
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            role = UserRole.ADMIN;
        } else if (request.getRole() != null && request.getRole().equalsIgnoreCase("MODERATOR")) {
            role = UserRole.MODERATOR;
        } else if (request.getRole() != null && request.getRole().equalsIgnoreCase("RECEPTIONIST")) {
            role = UserRole.RECEPTIONIST;
        } else if (request.getRole() != null && request.getRole().equalsIgnoreCase("MANAGER")){
            role = UserRole.MANAGER;
        } else {
            role = UserRole.CUSTOMER; // default
        }
        User user = new User(request.getFullName(),request.getEmail(), hashed,role);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}


