package com.bridgelabz.cryptotracker.user.service;

import com.bridgelabz.cryptotracker.user.entity.User;
import com.bridgelabz.cryptotracker.user.entity.Role;
import com.bridgelabz.cryptotracker.user.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String name, Integer userId, String email, String rawPassword, String roleName) {
        User user = new User();
        user.setName(name);
        user.setUserId(userId); 
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        Set<Role> roles = new HashSet<>();
        try {
            roles.add(Role.valueOf(roleName));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Invalid role: " + roleName);
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    // gets all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String loginUser(String email, String rawPassword) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new Exception("Invalid password");
        }

        return "Logged in successfully as: " + user.getName();
    }
}
