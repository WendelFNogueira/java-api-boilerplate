package br.com.wendelnogueira.javaapiboilerplate.controller;

import br.com.wendelnogueira.javaapiboilerplate.api.UsersApi;
import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    private final UsersService usersService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<String> createUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("createUser called by user: {}", auth.getName());
        UserDto userDto = userMapper.toDtoFromApi(user);
        usersService.createUser(userDto);
        return ResponseEntity.status(201).body("User created successfully");
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("deleteUser called by user: {}", auth.getName());
        usersService.deleteUser(id.longValue());
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("getUserByEmail called by user: {} for email: {}", auth.getName(), email);
        UserDto userDto = usersService.getUserByEmail(email);
        User apiUser = userMapper.toApiModel(userMapper.toEntity(userDto));
        return ResponseEntity.ok(apiUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("getUsers called by user: {}", auth.getName());
        List<UserDto> userDtos = usersService.getAllUsers();
        List<User> apiUsers = userDtos.stream()
                .map(userMapper::toEntity)
                .map(userMapper::toApiModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apiUsers);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(Integer id, User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("updateUser called by user: {}", auth.getName());
        UserDto userDto = userMapper.toDtoFromApi(user);
        usersService.updateUser(id.longValue(), userDto);
        return ResponseEntity.ok("User updated successfully");
    }
}
