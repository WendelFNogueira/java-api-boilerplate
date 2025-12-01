package br.com.wendelnogueira.javaapiboilerplate.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String role;
}
