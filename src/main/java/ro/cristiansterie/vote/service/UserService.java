package ro.cristiansterie.vote.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import ro.cristiansterie.vote.aspect.Loggable;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.Paging;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class UserService extends GenericService implements UserDetailsService {
    private UserRepository repo;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ALL, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_GET_ALL)
    public List<UserDTO> getAll() {
        return convert(repo.findAll());
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ALL, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_GET_ALL_FACE_IMAGES)
    public Map<Long, byte[]> getAllFaceImagesBase64() {
        return repo.findAll().stream().filter(user -> user.getFaceImage() != null)
                .collect(Collectors.toMap(UserDAO::getId, UserDAO::getFaceImage));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_FILTERED, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_GET_FILTERED)
    public List<UserDTO> getFiltered(UserFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return convert(repo.findAll(Example.of(convert(filter.getObject()), matcher), pageable).getContent());
    }

    @Loggable(action = AppConstants.EVENT_ACTION_COUNT_FILTERED, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_COUNT_FILTERED)
    public Long countFiltered(UserFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return repo.count(Example.of(convert(filter.getObject()), matcher));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ONE, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_GET_ONE)
    public UserDTO get(@NonNull Long id) {
        UserDAO returnable = repo.findById(id).orElse(null);

        // return user if exists
        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ONE, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_GET_VOTER_ONE)
    public UserVoterDTO getVoter(@NonNull Long id) {
        UserDAO returnable = repo.findById(id).orElse(null);

        // return user if exists
        return null != returnable && null != returnable.getId() ? convertToVoter(returnable) : null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ONE, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_GET_PROFILE)
    public UserVoterDTO getProfile(@NonNull Long id) {
        UserDAO returnable = repo.findById(id).orElse(null);

        // return user if exists
        return null != returnable && null != returnable.getId() ? convertToVoter(returnable) : null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_SAVE, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_USER_SAVE)
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

        return convert(repo.save(convert(user)));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_SAVE, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_PROFILE_SAVE)
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

        return convertToVoter(repo.save(convertFromVoter(user)));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_SAVE, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_PROFILE_REGISTER)
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

        return convertToVoter(repo.save(convertFromVoter(user)));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_DELETE, screen = AppConstants.EVENT_SCREEN_USERS, message = AppConstants.EVENT_USERS_DELETE)
    public Boolean delete(Long id) {
        try {
            repo.deleteById(id);

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
