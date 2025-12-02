package br.com.wendelnogueira.javaapiboilerplate.service;

import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.exception.ConflictException;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public UserEntity login(String email, String password) {
        logger.info("Attempting login for user: {}", email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        UserEntity user = (UserEntity) authentication.getPrincipal();
        logger.info("Login successful for user: {}", email);
        return user;
    }

    public UserDto registerUser(UserDto userDto) {
        logger.info("Registering user with email: {}", userDto.getEmail());
        try {
            UserEntity user = userMapper.toEntity(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            UserEntity savedUser = userRepository.save(user);
            logger.info("User registered successfully with email: {}", userDto.getEmail());
            return userMapper.toDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            logger.warn("Failed to register user with email: {} - User already exists", userDto.getEmail());
            throw new ConflictException("09", "User with this email already exists");
        } catch (Exception e) {
            logger.error("Unexpected error during user registration for email: {}", userDto.getEmail(), e);
            throw new RuntimeException("An error occurred while registering the user");
        }
    }
}
