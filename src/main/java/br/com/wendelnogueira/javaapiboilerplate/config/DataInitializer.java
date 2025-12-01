package br.com.wendelnogueira.javaapiboilerplate.config;

import br.com.wendelnogueira.javaapiboilerplate.dto.UserDto;
import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import br.com.wendelnogueira.javaapiboilerplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usersRepository.findByEmail("admin@example.com").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole(UserEntity.Role.ADMIN);
            usersRepository.save(admin);
        }

        if (usersRepository.findByEmail("user@example.com").isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setRole(UserEntity.Role.USER);
            usersRepository.save(user);
        }
    }
}
