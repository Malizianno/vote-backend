package ro.cristiansterie.vote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.util.UserRoleEnum;

public interface UserRepository extends JpaRepository<UserDAO, Long> {
    UserDAO findByUsername(String username);

    UserDAO findByCnp(Long cnp);

    long countByRole(UserRoleEnum role);

    // XXX: this is used only in development, please remove
    @Query(value = "SELECT * FROM users LIMIT :limited", nativeQuery = true)
    List<UserDAO> findLimitedActiveUsers(@Param("limited") int limited);

}
