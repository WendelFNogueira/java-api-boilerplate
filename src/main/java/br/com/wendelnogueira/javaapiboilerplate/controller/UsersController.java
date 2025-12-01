package br.com.wendelnogueira.javaapiboilerplate.controller;

import br.com.wendelnogueira.javaapiboilerplate.api.UsersApi;
import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UsersService usersService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<Void> createUser(User user) {
        UserDto userDto = userMapper.toEntityFromApi(user);
        usersService.createUser(userDto);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Integer id) {
        usersService.deleteUser(id.longValue());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<User> getUserById(Integer id) {
        UserDto userDto = usersService.getUserById(id.longValue());
        User apiUser = userMapper.toApiModel(userMapper.toEntity(userDto));
        return ResponseEntity.ok(apiUser);
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<UserDto> userDtos = usersService.getAllUsers();
        List<User> apiUsers = userDtos.stream()
                .map(userMapper::toEntity)
                .map(userMapper::toApiModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apiUsers);
    }

    @Override
    public ResponseEntity<Void> updateUser(Integer id, User user) {
        UserDto userDto = userMapper.toEntityFromApi(user);
        usersService.updateUser(id.longValue(), userDto);
        return ResponseEntity.ok().build();
    }
}
