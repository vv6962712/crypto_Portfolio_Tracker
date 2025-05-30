package com.bridgelabz.cryptotracker;

import com.bridgelabz.cryptotracker.user.entity.User;
import com.bridgelabz.cryptotracker.user.repository.UserRepository;
import com.bridgelabz.cryptotracker.user.service.UserService;
import com.bridgelabz.cryptotracker.user.entity.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock and @InjectMocks
    }

    @Test
    void registerUser_shouldSaveUserWithEncodedPasswordAndRole() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        userService.registerUser("John", 100, "john@example.com", "plainPassword", "ROLE_USER");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("John", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(Role.ROLE_USER));
    }

    @Test
    void registerUser_withInvalidRole_shouldThrowException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser("Jane", 101, "jane@example.com", "password", "INVALID_ROLE");
        });

        assertTrue(exception.getMessage().contains("Invalid role"));
    }

    @Test
    void loginUser_withValidCredentials_shouldReturnSuccessMessage() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPass");
        mockUser.setName("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("plainPass", "encodedPass")).thenReturn(true);

        String result = userService.loginUser("test@example.com", "plainPass");
        assertEquals("Logged in successfully as: Test User", result);
    }

    @Test
    void loginUser_withWrongPassword_shouldThrowException() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPass");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.loginUser("test@example.com", "wrongPass");
        });

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void loginUser_withNonExistentEmail_shouldThrowException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            userService.loginUser("nonexistent@example.com", "anyPassword");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllUsers_shouldReturnList() {
        User user1 = new User();
        user1.setEmail("vv6962711");
        user1.setName("vishnu");

        User user2 = new User();
        user2.setEmail("dinesh2244");
        user2.setName("dinesh");

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("vishnu", result.get(0).getName());
        assertEquals("vv6962711", result.get(0).getEmail());
        assertEquals("dinesh", result.get(1).getName());
        assertEquals("dinesh2244", result.get(1).getEmail());
    }
}
