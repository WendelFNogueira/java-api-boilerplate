package br.com.wendelnogueira.javaapiboilerplate.service;

import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginRequest;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginResponse;
import br.com.wendelnogueira.javaapiboilerplate.exception.UnauthorizedException;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import br.com.wendelnogueira.javaapiboilerplate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            UserEntity user = (UserEntity) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user.getUsername());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            return response;
        } catch (Exception e) {
            throw new UnauthorizedException("04", e.getMessage());
        }
    }

    public UserDto register(UserDto userDto) {
        UserEntity user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode("password")); // Default password
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
