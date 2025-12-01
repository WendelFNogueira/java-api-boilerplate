package br.com.wendelnogueira.javaapiboilerplate.integration;

import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import br.com.wendelnogueira.javaapiboilerplate.service.UsersService;
import br.com.wendelnogueira.javaapiboilerplate.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureWebMvc
class UsersControllerTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void getUsers_shouldReturn200() throws Exception {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");
        userRepository.save(userEntity);

        // Act & Assert
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void getUserByEmail_shouldReturn200_whenUserExists() throws Exception {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser" + System.currentTimeMillis());
        userEntity.setEmail("test" + System.currentTimeMillis() + "@example.com");
        userEntity.setPassword("password");
        UserEntity savedUser = userRepository.save(userEntity);

        // Act & Assert
        mockMvc.perform(get("/users/{email}", userEntity.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userEntity.getUsername()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void getUserByEmail_shouldReturn404_whenUserDoesNotExist() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/users/{email}", "nonexistent@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void createUser_shouldReturn201() throws Exception {
        // Arrange
        User user = new User();
        user.setName("newuser");
        user.setEmail("new@example.com");
        user.setPassword("password");

        ObjectMapper objectMapper = new ObjectMapper();

        // Act & Assert
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateUser_shouldReturn200_whenUserExists() throws Exception {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("olduser");
        userEntity.setEmail("old@example.com");
        userEntity.setPassword("password");
        UserEntity savedUser = userRepository.save(userEntity);

        User user = new User();
        user.setName("updateduser");
        user.setEmail("updated@example.com");
        user.setPassword("password");

        ObjectMapper objectMapper = new ObjectMapper();

        // Act & Assert
        mockMvc.perform(put("/users/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser_shouldReturn204_whenUserExists() throws Exception {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");
        UserEntity savedUser = userRepository.save(userEntity);

        // Act & Assert
        mockMvc.perform(delete("/users/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUsers_shouldReturn403_whenNotAuthenticated() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
