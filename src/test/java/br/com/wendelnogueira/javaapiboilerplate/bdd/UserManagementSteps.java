package br.com.wendelnogueira.javaapiboilerplate.bdd;

import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import br.com.wendelnogueira.javaapiboilerplate.service.UsersService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest
@Testcontainers
public class UserManagementSteps {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        mysql.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private UserDto createdUser;
    private List<UserDto> allUsers;
    private UserDto retrievedUser;
    private UserDto updatedUser;
    private Long userIdToDelete;

    @Given("the application is running")
    public void theApplicationIsRunning() {
        // Application is running via Spring Boot Test
    }

    @When("I create a user with name {string} and email {string}")
    public void iCreateAUserWithNameAndEmail(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setPassword("password123");
        userDto.setRole("USER");

        createdUser = usersService.createUser(userDto);
    }

    @Then("the user should be created successfully")
    public void theUserShouldBeCreatedSuccessfully() {
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
    }

    @Then("the response should contain the user details")
    public void theResponseShouldContainTheUserDetails() {
        assertNotNull(createdUser.getName());
        assertNotNull(createdUser.getEmail());
    }

    @Given("there is a user with name {string} and email {string}")
    public void thereIsAUserWithNameAndEmail(String name, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(name);
        userEntity.setEmail(email);
        userEntity.setPassword("password123");
        userEntity.setRole(UserEntity.Role.USER);
        userRepository.save(userEntity);
    }

    @When("I request all users")
    public void iRequestAllUsers() {
        allUsers = usersService.getAllUsers();
    }

    @Then("I should receive a list of users")
    public void iShouldReceiveAListOfUsers() {
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
    }

    @Then("the list should contain the user {string}")
    public void theListShouldContainTheUser(String name) {
        assertTrue(allUsers.stream().anyMatch(user -> name.equals(user.getName())));
    }

    @When("I request the user by email {string}")
    public void iRequestTheUserByEmail(String email) {
        retrievedUser = usersService.getUserByEmail(email);
    }

    @Then("I should receive the user details for {string}")
    public void iShouldReceiveTheUserDetailsFor(String name) {
        assertNotNull(retrievedUser);
        assertEquals(name, retrievedUser.getName());
    }

    @When("I update the user with email {string} to name {string} and email {string}")
    public void iUpdateTheUserWithEmailToNameAndEmail(String oldEmail, String newName, String newEmail) {
        UserDto existingUser = usersService.getUserByEmail(oldEmail);
        UserDto updateDto = new UserDto();
        updateDto.setName(newName);
        updateDto.setEmail(newEmail);
        updateDto.setRole(existingUser.getRole());
        updatedUser = usersService.updateUser(existingUser.getId().longValue(), updateDto);
    }

    @Then("the user should be updated successfully")
    public void theUserShouldBeUpdatedSuccessfully() {
        assertNotNull(updatedUser);
    }

    @When("I delete the user with email {string}")
    public void iDeleteTheUserWithEmail(String email) {
        UserDto userToDelete = usersService.getUserByEmail(email);
        userIdToDelete = userToDelete.getId().longValue();
        usersService.deleteUser(userIdToDelete);
    }

    @Then("the user should be deleted successfully")
    public void theUserShouldBeDeletedSuccessfully() {
        // Verify the user is deleted by trying to find it
        assertThrows(Exception.class, () -> usersService.getUserByEmail("charlie@example.com"));
    }
}
