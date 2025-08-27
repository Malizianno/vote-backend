package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import ro.cristiansterie.vote.dto.EventDTO;
import ro.cristiansterie.vote.dto.EventFilterDTO;
import ro.cristiansterie.vote.entity.EventDAO;
import ro.cristiansterie.vote.repository.EventRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class EventService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public List<EventDTO> getAll() {
        return convert(repo.findAll());
    }

    public List<EventDTO> getLast10() {
        // Get all events, sort by timestamp in descending order, and limit to 10
        return repo.findAll()
                .stream()
                .map(this::convert)
                .sorted(Comparator.comparing(EventDTO::getTimestamp).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getFiltered(EventFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return convert(repo.findAll(Example.of(convert(filter.getEvent()), matcher), pageable).getContent());
    }

    public int countFiltered(EventFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return (int) repo.count(Example.of(convert(filter.getEvent()), matcher));
    }

    public EventDTO get(@NonNull Integer id) {
        EventDAO returnable = repo.findById(id).orElse(null);
        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    public EventDTO save(EventDTO event) {
        if (null == event || !validateEvent(event)) {
            log.error("Cannot save event: {}", event);

            return null;
        }

        return convert(repo.save(convert(event)));
    }

    public boolean save(EventActionEnum action, EventScreenEnum screen, String message) {
        EventDTO event = new EventDTO();
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        var loggedRole = UserRoleEnum.valueOf(
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString());

        event.setUsername(
                loggedUsername.equals(AppConstants.ANONYMOUS_USER) ? AppConstants.ADMIN_USER : loggedUsername);
        event.setRole(loggedRole.equals(UserRoleEnum.ROLE_ANONYMOUS) ? UserRoleEnum.ADMIN : loggedRole);
        event.setAction(action);
        event.setScreen(screen);
        event.setTimestamp(String.valueOf(new Date().getTime()));
        event.setMessage(message);

        try {
            save(event);

            return true;
        } catch (Exception e) {
            log.error("Cannot save event: {}", e.getMessage());
        }

        return false;
    }

    public boolean delete(Integer id) {
        try {
            repo.deleteById(id);

            return true;
        } catch (Exception e) {
            log.error("Cannot delete event {} because: {}", id, e.getMessage());
        }

        return false;
    }

    // CONVERTERS

    protected EventDAO convert(EventDTO event) {
        return mapper.map(event, EventDAO.class);
    }

    protected EventDTO convert(EventDAO event) {
        return mapper.map(event, EventDTO.class);
    }

    protected List<EventDTO> convert(List<EventDAO> events) {
        return events.stream().map(this::convert).collect(Collectors.toList());
    }

    // VALIDATORS

    private boolean validateEvent(EventDTO event) {
        return event.getUsername() != null
                && !event.getUsername().isEmpty()
                && event.getRole() != null
                && event.getRole() != UserRoleEnum.ALL
                && event.getAction() != null
                && event.getAction() != EventActionEnum.ALL
                && event.getScreen() != null
                && event.getScreen() != EventScreenEnum.ALL;
    }

    private EventFilterDTO checkFilters(EventFilterDTO filter) {
        if (UserRoleEnum.ALL.equals(filter.getEvent().getRole())) {
            filter.getEvent().setRole(null);
        }

        if (EventActionEnum.ALL.equals(filter.getEvent().getAction())) {
            filter.getEvent().setAction(null);
        }

        if (EventScreenEnum.ALL.equals(filter.getEvent().getScreen())) {
            filter.getEvent().setScreen(null);
        }

        return filter;
    }
}
