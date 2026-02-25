package ro.cristiansterie.vote.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.aspect.Loggable;
import ro.cristiansterie.vote.dto.VoteDTO;
import ro.cristiansterie.vote.entity.VoteDAO;
import ro.cristiansterie.vote.repository.VoteRepository;
import ro.cristiansterie.vote.util.AppConstants;

@Service
public class VoteService extends GenericService {
    private UserService userService;
    private VoteRepository repo;

    public VoteService(UserService userService, VoteRepository repo) {
        this.userService = userService;
        this.repo = repo;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_FILTERED, screen = AppConstants.EVENT_SCREEN_VOTE, message = AppConstants.EVENT_VOTES_GET_FILTERED)
    public List<VoteDTO> getFiltered(@NonNull VoteDTO filter) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return convert(repo.findAll(Example.of(convert(filter), matcher)));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_VOTE, screen = AppConstants.EVENT_SCREEN_VOTE, message = AppConstants.EVENT_VOTES_TAKE_A_VOTE)
    public boolean takeAVote(VoteDTO newVote) {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            VoteDTO saved = convert(repo.save(convert(newVote)));

            this.userService.setHasVotedTrue(username);

            return null != saved && null != saved.getId();
        } catch (Exception e) {
            log.error("Exception while trying to save a vote: {}: \n{}", newVote, e);
        }

        return false;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_DELETE, screen = AppConstants.EVENT_SCREEN_VOTE, message = AppConstants.EVENT_VOTES_CLEAN_ALL_VOTES)
    public boolean cleanDBTable(long electionId) {
        try {
            // first check if there are any entries in DB table
            if (repo.count() > 0) {
                repo.deleteByElectionId(electionId);
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
