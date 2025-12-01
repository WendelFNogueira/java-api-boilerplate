package br.com.wendelnogueira.javaapiboilerplate.repository;

import br.com.wendelnogueira.javaapiboilerplate.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
}
