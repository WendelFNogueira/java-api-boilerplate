package br.com.wendelnogueira.javaapiboilerplate.unit;

import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginRequest;
import br.com.wendelnogueira.javaapiboilerplate.api.model.LoginResponse;
import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
import br.com.wendelnogueira.javaapiboilerplate.controller.AuthController;
import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.service.AuthService;
import br.com.wendelnogueira.javaapiboilerplate.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_shouldReturnLoginResponseWithToken_whenCredentialsAreValid() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setRole(UserEntity.Role.USER);

        when(authService.login("test@example.com", "password")).thenReturn(userEntity);
        when(jwtUtil.generateToken("test@example.com", "USER")).thenReturn("jwt-token");

        // Act
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().getToken());
        verify(authService).login("test@example.com", "password");
        verify(jwtUtil).generateToken("test@example.com", "USER");
    }

    @Test
    void register_shouldReturnCreatedUser_whenRegistrationIsSuccessful() {
        // Arrange
        User apiUser = new User();
        apiUser.setEmail("new@example.com");
        apiUser.setName("New User");

        UserDto userDto = new UserDto();
        userDto.setEmail("new@example.com");
        userDto.setName("New User");

        UserDto createdDto = new UserDto();
        createdDto.setId(1);
        createdDto.setEmail("new@example.com");
        createdDto.setName("New User");

        UserEntity createdEntity = new UserEntity();
        createdEntity.setId(1L);
        createdEntity.setEmail("new@example.com");
        createdEntity.setUsername("New User");

        User responseApiUser = new User();
        responseApiUser.setId(1);
        responseApiUser.setEmail("new@example.com");
        responseApiUser.setName("New User");

        when(userMapper.toDtoFromApi(apiUser)).thenReturn(userDto);
        when(authService.registerUser(userDto)).thenReturn(createdDto);
        when(userMapper.toEntity(createdDto)).thenReturn(createdEntity);
        when(userMapper.toApiModel(createdEntity)).thenReturn(responseApiUser);

        // Act
        ResponseEntity<User> response = authController.register(apiUser);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("new@example.com", response.getBody().getEmail());
        verify(authService).registerUser(userDto);
        verify(userMapper).toDtoFromApi(apiUser);
        verify(userMapper).toEntity(createdDto);
        verify(userMapper).toApiModel(createdEntity);
    }
}
