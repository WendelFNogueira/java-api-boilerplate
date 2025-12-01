package br.com.wendelnogueira.javaapiboilerplate.controller;

import br.com.wendelnogueira.javaapiboilerplate.api.AuthApi;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginRequest;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginResponse;
import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<User> register(User user) {
        UserDto userDto = userMapper.toDtoFromApi(user);
        UserDto savedUserDto = authService.register(userDto);
        User apiUser = userMapper.toApiModel(userMapper.toEntity(savedUserDto));
        return ResponseEntity.status(201).body(apiUser);
    }
}
