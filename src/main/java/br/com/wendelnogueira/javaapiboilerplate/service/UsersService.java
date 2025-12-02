package br.com.wendelnogueira.javaapiboilerplate.service;

import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.exception.ConflictException;
import br.com.wendelnogueira.javaapiboilerplate.exception.NotFoundException;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("05", "User not found with ID: " + id));
        return userMapper.toDto(user);
    }

    public UserDto getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("05", "User not found with email: " + email));
        return userMapper.toDto(user);
    }

    public UserDto createUser(UserDto userDto) {
        logger.info("Creating user with email: {}", userDto.getEmail());
        try {
            UserEntity user = userMapper.toEntity(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            UserEntity savedUser = userRepository.save(user);
            logger.info("User created successfully with email: {}", userDto.getEmail());
            return userMapper.toDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            logger.warn("Failed to create user with email: {} - User already exists", userDto.getEmail());
            throw new ConflictException("09", "User with this email already exists");
        } catch (Exception e) {
            logger.error("Unexpected error during user creation for email: {}", userDto.getEmail(), e);
            throw new RuntimeException("An error occurred while creating the user");
        }
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("05", "User not found with ID: " + id));
        user.setUsername(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(UserEntity.Role.valueOf(userDto.getRole()));
        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("05", "User not found with ID: " + id));
        userRepository.delete(user);
    }
}
