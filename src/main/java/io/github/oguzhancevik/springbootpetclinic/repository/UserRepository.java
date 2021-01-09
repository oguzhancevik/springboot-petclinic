package io.github.oguzhancevik.springbootpetclinic.repository;

import io.github.oguzhancevik.springbootpetclinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.authorities a where u.username = :username and u.enabled = true")
    User findByUsername(String username);

}
