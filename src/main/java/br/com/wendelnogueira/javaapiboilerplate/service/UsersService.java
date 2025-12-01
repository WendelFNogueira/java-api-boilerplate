package br.com.wendelnogueira.javaapiboilerplate.service;

import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.exception.NotFoundException;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public UserDto createUser(UserDto userDto) {
        UserEntity user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode("password")); // Set default password
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
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
