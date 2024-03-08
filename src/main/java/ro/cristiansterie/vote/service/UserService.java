package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;
import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.UserRepository;

@Service
public class UserService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<UserDTO> getAll() {
        return convert(repo.findAll());
    }

    public List<UserDTO> getFiltered(UserFilterDTO filter) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return convert(repo.findAll(Example.of(convert(filter.getUser()), matcher), pageable).getContent());
    }

    public int countFiltered(UserFilterDTO filter) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return (int) repo.count(Example.of(convert(filter.getUser()), matcher));
    }

    public UserDTO get(@NonNull Integer id) {
        UserDAO returnable = repo.findById(id).orElse(null);
        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    public UserDTO save(UserDTO user) {
        return null != user ? convert(repo.save(convert(user))) : null;
    }

    public boolean delete(Integer id) {
        try {
            repo.deleteById(id);

            return true;
        } catch (Exception e) {
            log.error("Cannot delete user {} because: {}", id, e);
        }

        return false;
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
}
