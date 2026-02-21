package ro.cristiansterie.vote.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

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

import ro.cristiansterie.vote.entity.EventDAO;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.Paging;
import ro.cristiansterie.vote.util.UserRoleEnum;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryIT {

    @Autowired
    private EventRepository repository;

    @Test
    void findAll_returnsAllEntities() {
        EventDAO event = new EventDAO();
        event.setAction(EventActionEnum.SAVE);
        event.setMessage("message");
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event.setRole(UserRoleEnum.ADMIN);
        event.setScreen(EventScreenEnum.DASHBOARD);
        event.setUsername("test");

        var savedEvent = repository.save(event);
        assertNotNull(savedEvent);

        List<EventDAO> allEvents = repository.findAll();

        assertNotNull(allEvents);
        assertEquals(1, allEvents.size());
    }

    @Test
    void findFiltered_returnsEntitiesMatchingFilters() {
        EventDAO event1 = new EventDAO();
        event1.setAction(EventActionEnum.SAVE);
        event1.setMessage("message1");
        event1.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event1.setRole(UserRoleEnum.ADMIN);
        event1.setScreen(EventScreenEnum.DASHBOARD);
        event1.setUsername("test");

        EventDAO event2 = new EventDAO();
        event2.setAction(EventActionEnum.DELETE);
        event2.setMessage("message2");
        event2.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event2.setRole(UserRoleEnum.VOTANT);
        event2.setScreen(EventScreenEnum.CANDIDATES);
        event2.setUsername("test");

        repository.save(event1);
        repository.save(event2);

        Paging paging = new Paging();
        EventDAO filter = new EventDAO();
        filter.setRole(UserRoleEnum.ADMIN);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(paging.getPage(), paging.getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        List<EventDAO> filteredEvents = repository.findAll(Example.of(filter, matcher), pageable).getContent();

        assertNotNull(filteredEvents);
        assertEquals(1, filteredEvents.size());
    }

    @Test
    void countFiltered_returnsCountOfEntitiesMatchingFilters() {
        EventDAO event1 = new EventDAO();
        event1.setAction(EventActionEnum.SAVE);
        event1.setMessage("message1");
        event1.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event1.setRole(UserRoleEnum.ADMIN);
        event1.setScreen(EventScreenEnum.DASHBOARD);
        event1.setUsername("test");

        EventDAO event2 = new EventDAO();
        event2.setAction(EventActionEnum.DELETE);
        event2.setMessage("message2");
        event2.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event2.setRole(UserRoleEnum.VOTANT);
        event2.setScreen(EventScreenEnum.CANDIDATES);
        event2.setUsername("test");

        repository.save(event1);
        repository.save(event2);

        EventDAO filter = new EventDAO();
        filter.setRole(UserRoleEnum.ADMIN);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        long count = repository.count(Example.of(filter, matcher));

        assertEquals(1, count);
    }

    @Test
    void findById_returnsEntityWithGivenId() {
        EventDAO event = new EventDAO();
        event.setAction(EventActionEnum.SAVE);
        event.setMessage("message");
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event.setRole(UserRoleEnum.ADMIN);
        event.setScreen(EventScreenEnum.DASHBOARD);
        event.setUsername("test");

        var savedEvent = repository.save(event);
        assertNotNull(savedEvent);

        var foundEvent = repository.findById(savedEvent.getId()).orElse(null);

        assertNotNull(foundEvent);
        assertEquals(savedEvent.getId(), foundEvent.getId());
    }

    @Test
    void save_persistsEntity() {
        EventDAO event = new EventDAO();
        event.setAction(EventActionEnum.SAVE);
        event.setMessage("message");
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event.setRole(UserRoleEnum.ADMIN);
        event.setScreen(EventScreenEnum.DASHBOARD);
        event.setUsername("test");

        var savedEvent = repository.save(event);

        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());
    }

    @Test
    void deleteById_removesEntity() {
        EventDAO event = new EventDAO();
        event.setAction(EventActionEnum.SAVE);
        event.setMessage("message");
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event.setRole(UserRoleEnum.ADMIN);
        event.setScreen(EventScreenEnum.DASHBOARD);
        event.setUsername("test");

        var savedEvent = repository.save(event);
        assertNotNull(savedEvent);

        repository.deleteById(savedEvent.getId());

        var foundEvent = repository.findById(savedEvent.getId()).orElse(null);

        assertNull(foundEvent);
    }
}
