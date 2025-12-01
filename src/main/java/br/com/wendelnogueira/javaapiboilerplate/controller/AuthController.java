package br.com.wendelnogueira.javaapiboilerplate.controller;

import br.com.wendelnogueira.javaapiboilerplate.api.AuthApi;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginRequest;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginResponse;
import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.service.UsersService;
import br.com.wendelnogueira.javaapiboilerplate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersService usersService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<User> register(User user) {
        UserDto userDto = userMapper.toDtoFromApi(user);
        UserDto created = usersService.createUser(userDto);
        User apiUser = userMapper.toApiModel(userMapper.toEntity(created));
        return ResponseEntity.status(201).body(apiUser);
    }
}
