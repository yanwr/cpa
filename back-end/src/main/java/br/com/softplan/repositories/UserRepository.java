package br.com.softplan.repositories;

import br.com.softplan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    User findByEmail(String email);

    @Query(value = "SELECT u from User u JOIN u.profile p WHERE p = 3")
    List<User> findFinishers();
}