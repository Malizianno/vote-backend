package ro.cristiansterie.vote.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ro.cristiansterie.vote.dto.EventDTO;
import ro.cristiansterie.vote.dto.EventFilterDTO;
import ro.cristiansterie.vote.entity.EventDAO;
import ro.cristiansterie.vote.repository.EventRepository;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.Paging;
import ro.cristiansterie.vote.util.UserRoleEnum;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventService service;

    @BeforeEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void getAll_returnsExpectedValue() {
        // Arrange
        EventDAO event1 = new EventDAO();
        event1.setId(1L);
        event1.setAction(EventActionEnum.GET_ALL);
        event1.setScreen(EventScreenEnum.NEWSFEED);
        event1.setTimestamp(String.valueOf(System.currentTimeMillis()));

        EventDAO event2 = new EventDAO();
        event2.setId(2L);
        event2.setAction(EventActionEnum.SAVE);
        event2.setScreen(EventScreenEnum.CANDIDATES);
        event2.setTimestamp(String.valueOf(System.currentTimeMillis()));

        when(repository.findAll()).thenReturn(List.of(event1, event2));

        // Act
        List<EventDTO> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void getLast10_returnsExpectedValue() {
        // Arrange
        EventDAO event1 = new EventDAO();
        event1.setId(1L);
        event1.setAction(EventActionEnum.GET_ALL);
        event1.setScreen(EventScreenEnum.NEWSFEED);
        event1.setTimestamp(String.valueOf(System.currentTimeMillis()));

        EventDAO event2 = new EventDAO();
        event2.setId(2L);
        event2.setAction(EventActionEnum.SAVE);
        event2.setScreen(EventScreenEnum.CANDIDATES);
        event2.setTimestamp(String.valueOf(System.currentTimeMillis()));

        when(repository.findAll()).thenReturn(List.of(event1, event2));

        // Act
        List<EventDTO> result = service.getLast10();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(repository, times(1)).findAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    void getFiltered_returnsExpectedValue() {
        // Arrange
        EventDAO event1 = new EventDAO();
        event1.setId(1L);
        event1.setAction(EventActionEnum.GET_ALL);
        event1.setScreen(EventScreenEnum.NEWSFEED);
        event1.setTimestamp(String.valueOf(System.currentTimeMillis()));

        EventDAO event2 = new EventDAO();
        event2.setId(2L);
        event2.setAction(EventActionEnum.SAVE);
        event2.setScreen(EventScreenEnum.CANDIDATES);
        event2.setTimestamp(String.valueOf(System.currentTimeMillis()));

        Paging paging = new Paging();
        EventFilterDTO filter = new EventFilterDTO();
        filter.setEvent(service.convert(event1));
        filter.setPaging(paging);

        when(repository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(new PageImpl<EventDAO>(List.of(event1)));

        // Act
        List<EventDTO> result = service.getFiltered(filter);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(EventActionEnum.GET_ALL, result.get(0).getAction());

        verify(repository, times(1)).findAll(any(Example.class), any(Pageable.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void countFiltered_returnsExpectedValue() {
        // Arrange
        EventDAO event1 = new EventDAO();
        event1.setId(1L);
        event1.setAction(EventActionEnum.GET_ALL);
        event1.setScreen(EventScreenEnum.NEWSFEED);
        event1.setTimestamp(String.valueOf(System.currentTimeMillis()));

        EventFilterDTO filter = new EventFilterDTO();
        filter.setEvent(service.convert(event1));

        when(repository.count(any(Example.class))).thenReturn(1L);

        // Act
        long count = service.countFiltered(filter);

        // Assert
        assertEquals(1, count);

        verify(repository, times(1)).count(any(Example.class));
    }

    @Test
    void get_returnsExpectedValue() {
        // Arrange
        Long eventId = 1L;

        EventDAO event = new EventDAO();
        event.setId(eventId);
        event.setAction(EventActionEnum.GET_ALL);
        event.setScreen(EventScreenEnum.NEWSFEED);
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));

        when(repository.findById(eventId)).thenReturn(java.util.Optional.of(event));

        // Act
        EventDTO result = service.get(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(eventId, result.getId());
        assertEquals(EventActionEnum.GET_ALL, result.getAction());

        verify(repository, times(1)).findById(eventId);
    }

    @Test
    void save_persistsEntity() {
        // Arrange
        EventDTO event = new EventDTO();
        event.setUsername("test");
        event.setRole(UserRoleEnum.ROLE_ANONYMOUS);
        event.setAction(EventActionEnum.SAVE);
        event.setScreen(EventScreenEnum.CANDIDATES);
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));

        EventDAO savedEvent = new EventDAO();
        savedEvent.setId(1L);
        savedEvent.setUsername(event.getUsername());
        savedEvent.setRole(event.getRole());
        savedEvent.setAction(event.getAction());
        savedEvent.setScreen(event.getScreen());
        savedEvent.setTimestamp(event.getTimestamp());

        when(repository.save(any(EventDAO.class))).thenReturn(savedEvent);

        // Act
        EventDTO result = service.save(event);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals(EventActionEnum.SAVE, result.getAction());

        verify(repository, times(1)).save(any(EventDAO.class));
    }

    @Test
    void save_persistsEntityWithExtraValidation() {
        // Arrange
        EventDTO event = new EventDTO();
        event.setAction(EventActionEnum.SAVE);
        event.setScreen(EventScreenEnum.CANDIDATES);
        event.setMessage("message");
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));

        EventDAO savedEvent = new EventDAO();
        savedEvent.setId(1L);
        savedEvent.setAction(event.getAction());
        savedEvent.setScreen(event.getScreen());
        savedEvent.setTimestamp(event.getTimestamp());

        when(repository.save(any(EventDAO.class))).thenReturn(savedEvent);

        // Act
        boolean result = service.save(EventActionEnum.SAVE, EventScreenEnum.CANDIDATES, "message");

        // Assert
        assertNotNull(result);
        assertTrue(result);

        verify(repository, times(1)).save(any(EventDAO.class));
    }

    @Test
    void delete_removesEntity() {
        // Arrange
        Long eventId = 1L;

        // Act
        service.delete(eventId);

        // Assert
        verify(repository, times(1)).deleteById(eventId);
    }
}
