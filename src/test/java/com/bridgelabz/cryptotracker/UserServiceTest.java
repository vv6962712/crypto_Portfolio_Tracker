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
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void registerUser_shouldSaveUserWithEncodedPasswordAndRole() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        userService.registerUser("Vishnu", 100, "vishnu@email.com", "plainPassword", "ROLE_USER");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("Vishnu", savedUser.getName());
        assertEquals("vishnu@email.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(Role.ROLE_USER));
    }

    @Test
    void registerUser_withInvalidRole_shouldThrowException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser("Dinesh", 101, "dinesh@email.com", "password", "INVALID_ROLE");
        });

        assertTrue(exception.getMessage().contains("Invalid role"));
    }

    @Test
    void loginUser_withValidCredentials_shouldReturnSuccessMessage() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("vishnu@email.com");
        mockUser.setPassword("encodedPass");
        mockUser.setName("Vishnu");

        when(userRepository.findByEmail("vishnu@email.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("plainPass", "encodedPass")).thenReturn(true);

        String result = userService.loginUser("vishnu@email.com", "plainPass");
        assertEquals("Logged in successfully as: Vishnu", result);
    }

    @Test
    void loginUser_withWrongPassword_shouldThrowException() {
        User mockUser = new User();
        mockUser.setEmail("dinesh@email.com");
        mockUser.setPassword("encodedPass");

        when(userRepository.findByEmail("dinesh@email.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.loginUser("dinesh@email.com", "wrongPass");
        });

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void loginUser_withNonExistentEmail_shouldThrowException() {
        when(userRepository.findByEmail("nonexistent@email.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            userService.loginUser("nonexistent@email.com", "anyPassword");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllUsers_shouldReturnList() {
        User user1 = new User();
        user1.setEmail("vishnu@email.com");
        user1.setName("Vishnu");

        User user2 = new User();
        user2.setEmail("dinesh@email.com");
        user2.setName("Dinesh");

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("Vishnu", result.get(0).getName());
        assertEquals("vishnu@email.com", result.get(0).getEmail());
        assertEquals("Dinesh", result.get(1).getName());
        assertEquals("dinesh@email.com", result.get(1).getEmail());
    }
}
