package br.com.wendelnogueira.javaapiboilerplate.mapper;

import br.com.wendelnogueira.javaapiboilerplate.api.model.User;
import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "username")
    @Mapping(target = "email", source = "email")
    User toApiModel(UserEntity userEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", expression = "java(UserEntity.Role.USER)")
    UserEntity toEntity(UserDto userDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", expression = "java(UserEntity.Role.USER)")
    UserEntity toEntityFromApi(User user);
}
