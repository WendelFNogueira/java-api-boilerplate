package br.com.wendelnogueira.javaapiboilerplate.unit;

import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.exception.NotFoundException;
import br.com.wendelnogueira.javaapiboilerplate.mapper.UserMapper;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import br.com.wendelnogueira.javaapiboilerplate.service.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UsersService usersService;

    @Test
    void getAllUsers_shouldReturnListOfUserDtos() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("testuser");

        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Act
        List<UserDto> result = usersService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getName());
        verify(userRepository).findAll();
        verify(userMapper).toDto(userEntity);
    }

    @Test
    void getUserById_shouldReturnUserDto_whenUserExists() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Act
        UserDto result = usersService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getName());
        verify(userRepository).findById(1L);
        verify(userMapper).toDto(userEntity);
    }

    @Test
    void getUserById_shouldThrowNotFoundException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> usersService.getUserById(1L));
        assertEquals("05", exception.getCode());
        verify(userRepository).findById(1L);
    }

    @Test
    void createUser_shouldReturnCreatedUserDto() {
        // Arrange
        UserDto inputDto = new UserDto();
        inputDto.setName("newuser");
        inputDto.setEmail("new@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("newuser");
        userEntity.setEmail("new@example.com");

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);
        savedEntity.setUsername("newuser");

        UserDto outputDto = new UserDto();
        outputDto.setId(1);
        outputDto.setName("newuser");

        when(userMapper.toEntity(inputDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedEntity);
        when(userMapper.toDto(savedEntity)).thenReturn(outputDto);

        // Act
        UserDto result = usersService.createUser(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("newuser", result.getName());
        verify(userMapper).toEntity(inputDto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toDto(savedEntity);
    }

    @Test
    void updateUser_shouldReturnUpdatedUserDto_whenUserExists() {
        // Arrange
        UserDto inputDto = new UserDto();
        inputDto.setName("updateduser");
        inputDto.setEmail("updated@example.com");
        inputDto.setRole("USER");

        UserEntity existingEntity = new UserEntity();
        existingEntity.setId(1L);
        existingEntity.setUsername("olduser");

        UserEntity updatedEntity = new UserEntity();
        updatedEntity.setId(1L);
        updatedEntity.setUsername("updateduser");
        updatedEntity.setRole(UserEntity.Role.USER);

        UserDto outputDto = new UserDto();
        outputDto.setId(1);
        outputDto.setName("updateduser");
        outputDto.setRole("USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedEntity);
        when(userMapper.toDto(updatedEntity)).thenReturn(outputDto);

        // Act
        UserDto result = usersService.updateUser(1L, inputDto);

        // Assert
        assertNotNull(result);
        assertEquals("updateduser", result.getName());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingEntity);
        verify(userMapper).toDto(updatedEntity);
    }

    @Test
    void updateUser_shouldThrowNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UserDto inputDto = new UserDto();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> usersService.updateUser(1L, inputDto));
        assertEquals("05", exception.getCode());
        verify(userRepository).findById(1L);
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Act
        usersService.deleteUser(1L);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).delete(userEntity);
    }

    @Test
    void deleteUser_shouldThrowNotFoundException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> usersService.deleteUser(1L));
        assertEquals("05", exception.getCode());
        verify(userRepository).findById(1L);
    }
}
