package br.com.wendelnogueira.javaapiboilerplate.controller;

import br.com.wendelnogueira.javaapiboilerplate.api.AuthApi;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginRequest;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginResponse;
import br.com.wendelnogueira.javaapiboilerplate.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
