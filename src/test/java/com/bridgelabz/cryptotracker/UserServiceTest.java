package com.bridgelabz.cryptotracker;

import com.bridgelabz.cryptotracker.user.entity.Role;
import com.bridgelabz.cryptotracker.user.entity.User;
import com.bridgelabz.cryptotracker.user.repository.UserRepository;
import com.bridgelabz.cryptotracker.user.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_withValidRole() {
        String rawPassword = "password123";
        String encodedPassword = "encodedPass";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userService.registerUser("Vishnu", 101, "vishnu@example.com", rawPassword, "USER");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("Vishnu", savedUser.getName());
        assertEquals(101, savedUser.getUserId());
        assertEquals("vishnu@example.com", savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(Role.ROLE_USER));
    }

    @Test
    void testRegisterUser_withInvalidRole() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser("Dinesh", 102, "dinesh@example.com", "pass", "INVALID"));

        assertEquals("Invalid role: INVALID", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testLoginUser_withValidCredentials() throws Exception {
        String email = "vishnu@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encoded";

        User user = new User();
        user.setName("Vishnu");
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        String result = userService.loginUser(email, rawPassword);

        assertEquals("Logged in successfully as: Vishnu", result);
    }

    @Test
    void testLoginUser_withWrongPassword() {
        String email = "vishnu@example.com";
        String rawPassword = "wrongPass";
        String encodedPassword = "encoded";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () ->
                userService.loginUser(email, rawPassword));

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void testLoginUser_userNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                userService.loginUser("notfound@example.com", "pass"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetUserByEmail_found() {
        User user = new User();
        user.setEmail("vishnu@example.com");

        when(userRepository.findByEmail("vishnu@example.com")).thenReturn(Optional.of(user));

        User found = userService.getUserByEmail("vishnu@example.com");

        assertEquals("vishnu@example.com", found.getEmail());
    }

    @Test
    void testGetUserByEmail_notFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.getUserByEmail("missing@example.com"));

        assertEquals("User not found", exception.getMessage());
    }
}
