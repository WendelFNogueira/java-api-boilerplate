package br.com.wendelnogueira.javaapiboilerplate.repository;

import br.com.wendelnogueira.javaapiboilerplate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
