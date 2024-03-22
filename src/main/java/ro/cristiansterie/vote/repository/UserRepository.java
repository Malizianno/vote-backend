package ro.cristiansterie.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.UserDAO;

public interface UserRepository extends JpaRepository<UserDAO, Integer> {
    UserDAO findByUsername(String username);
}
