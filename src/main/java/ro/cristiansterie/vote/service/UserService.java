package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;
import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class UserService extends GenericService implements UserDetailsService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private UserRepository repo;
    private PasswordEncoder passwordEncoder;
    private final EventService events;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder, EventService events) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.events = events;
    }

    public List<UserDTO> getAll() {
        // save event
        events.save(EventActionEnum.GET_ALL, EventScreenEnum.USERS, AppConstants.EVENT_USERS_GET_ALL);

        return convert(repo.findAll());
    }

    public List<UserDTO> getFiltered(UserFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        // save event
        events.save(EventActionEnum.GET_FILTERED, EventScreenEnum.USERS,
                AppConstants.EVENT_USERS_GET_FILTERED);

        return convert(repo.findAll(Example.of(convert(filter.getUser()), matcher), pageable).getContent());
    }

    public int countFiltered(UserFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        // save event
        events.save(EventActionEnum.COUNT_FILTERED, EventScreenEnum.USERS,
                AppConstants.EVENT_USERS_COUNT_FILTERED);

        return (int) repo.count(Example.of(convert(filter.getUser()), matcher));
    }

    public UserDTO get(@NonNull Integer id) {
        UserDAO returnable = repo.findById(id).orElse(null);

        // save event
        events.save(EventActionEnum.GET, EventScreenEnum.USERS, AppConstants.EVENT_USERS_GET_ONE + id);
        // return user if exists
        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    public UserDTO save(UserDTO user) {
        if (null == user || !validateUserRole(user)) {
            log.error("Cannot save user: {}", user);

            return null;
        }

        UserDAO found = repo.findByUsername(user.getUsername());

        // in case new to DB encode password, otherwise update everything else
        if (null == found) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        var saved = repo.save(convert(user));

        // save event
        events.save(EventActionEnum.CREATE, EventScreenEnum.USERS,
                AppConstants.EVENT_USERS_SAVE + saved.getId());

        return convert(saved);
    }

    public boolean delete(Integer id) {
        try {
            repo.deleteById(id);

            // save event
            events.save(EventActionEnum.DELETE, EventScreenEnum.USERS, AppConstants.EVENT_USERS_DELETE + id);

            return true;
        } catch (Exception e) {
            log.error("Cannot delete user {} because: {}", id, e.getMessage());
        }

        return false;
    }

    public boolean hasVotedByUsername(String username) {
        UserDAO user = this.repo.findByUsername(username);

        if (null == user || null == user.getHasVoted()) {
            return false;
        }

        return user.getHasVoted();
    }

    /**
     * Use this only in case of voting by an user
     * 
     * @param username to vote
     * @return if the user has successfully voted
     */
    public boolean setHasVoted(String username) {
        UserDAO user = this.repo.findByUsername(username);

        if (null == user) {
            return false;
        }

        user.setHasVoted(true);

        this.save(convert(user));

        return true;
    }

    public UserDTO getByUsername(String username) {
        if (null == username || username.isBlank()) {
            return null;
        }

        return convert(this.repo.findByUsername(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDAO found = this.repo.findByUsername(username);

        if (null == found || null == found.getUsername()) {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(found.getRole().toString()));

        return new User(found.getUsername(), found.getPassword(), authorities);
    }

    private UserFilterDTO checkFilters(UserFilterDTO filter) {
        if (UserRoleEnum.ALL.equals(filter.getUser().getRole())) {
            filter.getUser().setRole(null);
        }

        return filter;
    }

    // CONVERTERS

    protected UserDAO convert(UserDTO user) {
        return mapper.map(user, UserDAO.class);
    }

    protected UserDTO convert(UserDAO user) {
        return mapper.map(user, UserDTO.class);
    }

    protected List<UserDTO> convert(List<UserDAO> users) {
        return users.stream().map(this::convert).collect(Collectors.toList());
    }

    // VALIDATORS

    private boolean validateUserRole(UserDTO user) {
        return null != user.getRole()
                && (UserRoleEnum.ADMIN.equals(user.getRole()) || UserRoleEnum.VOTANT.equals(user.getRole()));
    }
}
