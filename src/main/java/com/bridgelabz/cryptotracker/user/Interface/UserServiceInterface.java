package com.bridgelabz.cryptotracker.user.Interface;



import com.bridgelabz.cryptotracker.user.entity.User;

import java.util.List;

public interface UserServiceInterface {

    void registerUser(String name, Integer userId, String email, String rawPassword, String roleName);

    List<User> getAllUsers();

    String loginUser(String email, String rawPassword) throws Exception;
}
