package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.Paging;
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

    public Map<Long, byte[]> getAllFaceImagesBase64() {
        // save event
        events.save(EventActionEnum.GET_ALL, EventScreenEnum.USERS, AppConstants.EVENT_USERS_GET_ALL_FACE_IMAGES);

        return repo.findAll().stream().filter(user -> user.getFaceImage() != null)
                .collect(Collectors.toMap(UserDAO::getId, UserDAO::getFaceImage));
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

        return convert(repo.findAll(Example.of(convert(filter.getObject()), matcher), pageable).getContent());
    }

    public Long countFiltered(UserFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        // save event
        events.save(EventActionEnum.COUNT_FILTERED, EventScreenEnum.USERS,
                AppConstants.EVENT_USERS_COUNT_FILTERED);

        return repo.count(Example.of(convert(filter.getObject()), matcher));
    }

    public UserDTO get(@NonNull Long id) {
        UserDAO returnable = repo.findById(id).orElse(null);

        // save event
        events.save(EventActionEnum.GET, EventScreenEnum.USERS, AppConstants.EVENT_USERS_GET_ONE + id);
        // return user if exists
        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    public UserVoterDTO getVoter(@NonNull Long id) {
        UserDAO returnable = repo.findById(id).orElse(null);

        // save event
        events.save(EventActionEnum.GET, EventScreenEnum.USERS, AppConstants.EVENT_USERS_GET_ONE + id);
        // return user if exists
        return null != returnable && null != returnable.getId() ? convertToVoter(returnable) : null;
    }

    public UserVoterDTO getProfile(@NonNull Long id) {
        UserDAO returnable = repo.findById(id).orElse(null);

        // save event
        events.save(EventActionEnum.GET, EventScreenEnum.USERS, AppConstants.EVENT_USERS_GET_PROFILE + id);
        // return user if exists
        return null != returnable && null != returnable.getId() ? convertToVoter(returnable) : null;
    }

    public UserDTO save(UserDTO user) {
        if (null == user || !validateUserRole(user)) {
            log.error("Cannot save user: {}", user);

            return null;
        }

        UserDAO found = repo.findByUsername(user.getUsername());

        // in case new to DB encode password, otherwise update everything else
        if (shouldBeThePasswordEncoded(user, found)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(found.getPassword());
        }

        var saved = repo.save(convert(user));

        // save event
        events.save(EventActionEnum.CREATE, EventScreenEnum.USERS,
                AppConstants.EVENT_USERS_USER_SAVE + saved.getId());

        return convert(saved);
    }

    public UserVoterDTO saveProfile(UserVoterDTO user) {
        if (null == user || !validateUserRole(user)) {
            log.error("Cannot save user: {}", user);

            return null;
        }

        UserDAO found = repo.findByUsername(user.getUsername());

        // in case new to DB encode password, otherwise update everything else
        if (shouldBeThePasswordEncoded(user, found)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(found.getPassword());
        }

        UserDAO saved = repo.save(convertFromVoter(user));

        // save event
        events.save(EventActionEnum.CREATE, EventScreenEnum.USERS,
                AppConstants.EVENT_USERS_PROFILE_SAVE + saved.getId());

        return convertToVoter(saved);
    }

    public UserVoterDTO registerProfile(UserVoterDTO user) {
        if (null == user) {
            log.error("Cannot register user: {}", user);

            return null;
        }

        UserDAO found = repo.findByCnp(user.getCnp());
        if (null != found) {
            log.error("Cannot register user, cnp already exists: {}", user.getCnp());

            return null;
        }

        user.setRole(UserRoleEnum.VOTANT);
        user.setHasVoted(false);
        user.setUsername(null);
        user.setPassword(null);

        UserDAO saved = repo.save(convertFromVoter(user));

        // save event
        events.save(EventActionEnum.CREATE, EventScreenEnum.USERS,
                AppConstants.EVENT_USERS_PROFILE_REGISTER + saved.getId());

        return convertToVoter(saved);
    }

    public boolean delete(Long id) {
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
    public boolean setHasVotedTrue(String username) {
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
        UserDAO found = null;
        String foundUsername = null;
        String foundPassword = null;

        try {
            long id = Long.parseLong(username);

            Optional<UserDAO> optional = this.repo.findById(id);
            if (optional.isPresent()) {
                found = optional.get();
                foundUsername = String.valueOf(id);
                foundPassword = String.valueOf(id);

                log.info("Logging VOTANT with id: {}!", id);
            }
        } catch (NumberFormatException nfe) {
            found = this.repo.findByUsername(username);
            foundUsername = found.getUsername();
            foundPassword = found.getPassword();

            log.info("Logging ADMIN with username: {}", username);
        }

        if (null == found || null == foundUsername) {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(found.getRole().toString()));

        return new User(foundUsername, foundPassword, authorities);
    }

    private UserFilterDTO checkFilters(UserFilterDTO filter) {
        if (filter.getPaging() == null) {
            filter.setPaging(new Paging());
        }

        if (filter.getObject() == null) {
            filter.setObject(new UserDTO());
        }

        if (UserRoleEnum.ALL.equals(filter.getObject().getRole())) {
            filter.getObject().setRole(null);
        }

        return filter;
    }

    private boolean shouldBeThePasswordEncoded(UserDTO user, UserDAO found) {
        return null == found || (null != user && null != user.getPassword() && null != found.getPassword()
                && !user.getPassword().equals(found.getPassword()));

    }

    // CONVERTERS

    protected UserDAO convert(UserDTO user) {
        return mapper.map(user, UserDAO.class);
    }

    protected UserDTO convert(UserDAO user) {
        return mapper.map(user, UserDTO.class);
    }

    protected UserVoterDTO convertToVoter(UserDAO user) {
        return mapper.map(user, UserVoterDTO.class);
    }

    protected UserDAO convertFromVoter(UserVoterDTO user) {
        return mapper.map(user, UserDAO.class);
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
