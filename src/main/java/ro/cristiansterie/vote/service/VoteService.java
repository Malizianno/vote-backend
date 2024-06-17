package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.VoteDTO;
import ro.cristiansterie.vote.entity.VoteDAO;
import ro.cristiansterie.vote.repository.VoteRepository;

@Service
public class VoteService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private UserService userService;
    private VoteRepository repo;

    public VoteService(UserService userService, VoteRepository repo) {
        this.userService = userService;
        this.repo = repo;
    }

    // WIP: this should return statistic on votes only, should check bussiness logic

    public List<VoteDTO> getAll() {
        return convert(repo.findAll());
    }

    public List<VoteDTO> getFiltered(@NonNull VoteDTO filter) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return convert(repo.findAll(Example.of(convert(filter), matcher)));
    }

    public boolean takeAVote(VoteDTO newVote) {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            VoteDTO saved = convert(repo.save(convert(newVote)));

            this.userService.setHasVoted(username);

            return null != saved && null != saved.getId();
        } catch (Exception e) {
            log.error("Exception while trying to save a vote: {}: \n{}", newVote, e);
        }

        return false;
    }

    public boolean cleanDBTable() {
        try {
            // first check if there are any entries in DB table
            if (repo.count() > 0) {
                repo.deleteAll();
            }

            return true;
        } catch (Exception e) {
            log.error("Exception while trying to clean DB Table votes: {}", e.getMessage());
        }

        return false;
    }

    // CONVERTERS

    protected VoteDAO convert(VoteDTO vote) {
        return mapper.map(vote, VoteDAO.class);
    }

    protected VoteDTO convert(VoteDAO vote) {
        return mapper.map(vote, VoteDTO.class);
    }

    protected List<VoteDTO> convert(List<VoteDAO> votes) {
        return votes.stream().map(this::convert).collect(Collectors.toList());
    }
}
