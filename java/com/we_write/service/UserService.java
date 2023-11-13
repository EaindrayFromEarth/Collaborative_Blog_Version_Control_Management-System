package com.we_write.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.we_write.entity.User;
import com.we_write.exception.EmailAlreadyExistsException;
import com.we_write.exception.ResourceNotFoundException;
import com.we_write.exception.UsernameAlreadyExistsException;
import com.we_write.payload.JWTAuthResponseDto;
import com.we_write.payload.UserDto;
import com.we_write.repository.RoleRepository;
import com.we_write.repository.UserRepository;
import com.we_write.security.JwtTokenProvider;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    

    public User registerUser(UserDto userDto) {
        // Check if the username or email already exists
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already taken!");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered!");
        }

        // Create a new user entity and set properties
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        // Set user roles/authorities as needed

        // Save the user entity to the database
        return userRepository.save(user);
    }
    
    public String loginUser(UserDto loginRequest) {
        // Authenticate the user using Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // If authentication is successful, generate a JWT token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }
    
    // Update loginUser to return JWTAuthResponseDto
    public JWTAuthResponseDto authenticateUser(UserDto loginRequest) {
        // Authenticate the user using Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // If authentication is successful, generate a JWT token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        // Return JWTAuthResponseDto
        return new JWTAuthResponseDto(token);
    }
    
    public User getUserByUsername(User createdBy) {
        // Use the UserRepository to find a user by their username
        User user = userRepository.findByUsername(createdBy)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + createdBy));

        return user;
    }
    public User getUserByUsername(String createdBy) {
        // Use the UserRepository to find a user by their username
        User user = userRepository.findByUsername(createdBy)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + createdBy));

        return user;
    }

    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }








}
