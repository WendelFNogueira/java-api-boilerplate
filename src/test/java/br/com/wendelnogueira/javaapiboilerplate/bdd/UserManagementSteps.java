package br.com.wendelnogueira.javaapiboilerplate.bdd;

import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
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

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
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

    @When("I request the user by ID")
    public void iRequestTheUserByID() {
        // Assuming we have a way to get the last created user ID, for simplicity, get the first user
        List<UserDto> users = usersService.getAllUsers();
        if (!users.isEmpty()) {
            retrievedUser = usersService.getUserById(users.get(0).getId().longValue());
        }
    }

    @Then("I should receive the user details for {string}")
    public void iShouldReceiveTheUserDetailsFor(String name) {
        assertNotNull(retrievedUser);
        assertEquals(name, retrievedUser.getName());
    }

    @When("I update the user with name {string} and email {string}")
    public void iUpdateTheUserWithNameAndEmail(String name, String email) {
        List<UserDto> users = usersService.getAllUsers();
        if (!users.isEmpty()) {
            UserDto updateDto = new UserDto();
            updateDto.setName(name);
            updateDto.setEmail(email);
            updatedUser = usersService.updateUser(users.get(0).getId().longValue(), updateDto);
        }
    }

    @Then("the user should be updated successfully")
    public void theUserShouldBeUpdatedSuccessfully() {
        assertNotNull(updatedUser);
    }

    @When("I delete the user")
    public void iDeleteTheUser() {
        List<UserDto> users = usersService.getAllUsers();
        if (!users.isEmpty()) {
            userIdToDelete = users.get(0).getId().longValue();
            usersService.deleteUser(userIdToDelete);
        }
    }

    @Then("the user should be deleted successfully")
    public void theUserShouldBeDeletedSuccessfully() {
        assertThrows(Exception.class, () -> usersService.getUserById(userIdToDelete));
    }
}
