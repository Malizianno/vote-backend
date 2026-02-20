package ro.cristiansterie.vote.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.util.Paging;
import ro.cristiansterie.vote.util.UserGenderEnum;
import ro.cristiansterie.vote.util.UserNationalityEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIT {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void cleanupDB() {
        repository.deleteAll();
    }

    @Test
    void findAll_returnsAllEntities() {
        UserDAO user = createUserEntities(1).get(0);
        repository.save(user);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findFiltered_returnsEntitiesMatchingFilters() {
        Paging paging = new Paging();
        int count = 3;
        List<UserDAO> users = createUserEntities(count);

        for (int i = 0; i < count; i++) {
            repository.save(users.get(i));
        }

        UserDAO filter = new UserDAO();
        filter.setUsername("test1");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Pageable pageable = PageRequest.of(paging.getPage(), paging.getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        assertEquals(1, repository.findAll(Example.of(filter, matcher), pageable).getTotalElements());
    }

    @Test
    void countFiltered_returnsCountOfEntitiesMatchingFilters() {
        int count = 3;
        List<UserDAO> users = createUserEntities(count);

        for (int i = 0; i < count; i++) {
            repository.save(users.get(i));
        }

        UserDAO filter = new UserDAO();
        filter.setUsername("test");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        assertEquals(count, repository.count(Example.of(filter, matcher)));
    }

    @Test
    void findById_returnsEntityWithGivenId() {
        UserDAO user = createUserEntities(1).get(0);
        var savedUser = repository.save(user);
        var foundUser = repository.findById(savedUser.getId()).orElse(null);

        assertEquals(savedUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void findByCnp_returnsEntityWithGivenCnp() {
        UserDAO user = createUserEntities(1).get(0);

        UserDAO savedUser = repository.save(user);
        UserDAO foundUser = repository.findByCnp(savedUser.getCnp());

        assertEquals(savedUser.getCnp(), foundUser.getCnp());
    }

    @Test
    void findByUsername_returnsEntityWithGivenUsername() {
        UserDAO user = createUserEntities(1).get(0);

        UserDAO savedUser = repository.save(user);
        UserDAO foundUser = repository.findByUsername(savedUser.getUsername());

        assertEquals(savedUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void save_persistsEntity() {
        UserDAO user = createUserEntities(1).get(0);

        var savedUser = repository.save(user);

        assertNotNull(savedUser.getId());
    }

    @Test
    void deleteById_removesEntity() {
        UserDAO user = createUserEntities(1).get(0);

        var savedUser = repository.save(user);

        repository.deleteById(savedUser.getId());

        assertEquals(0, repository.count());
    }

    private List<UserDAO> createUserEntities(int count) {
        List<UserDAO> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            UserDAO user = new UserDAO();

            user.setId(1000L + i);
            user.setUsername("test" + i);
            user.setPassword("password");
            user.setCnp(1234567890123456L + i);
            user.setFaceImage(null);
            user.setFirstname("firstname");
            user.setGender(UserGenderEnum.OTHER);
            user.setHasVoted(true);
            user.setIdImage(null);
            user.setIdSeries("idseries");
            user.setLastname("Lastname");
            user.setNationality(UserNationalityEnum.FOREIGNER);
            user.setResidenceAddress("residence address");
            user.setRole(UserRoleEnum.VOTANT);
            user.setValidityEndDate(2L);
            user.setValidityStartDate(1L);

            users.add(user);
        }

        return users;
    }
}
