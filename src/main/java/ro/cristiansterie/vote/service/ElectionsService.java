package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.VoteDTO;
import ro.cristiansterie.vote.properties.ElectionProperties;

// WIP: this class executes elections, take care of voting process alltogether
@Service
public class ElectionsService extends GenericService {

    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ElectionProperties electionProps;
    private VoteService voteService;
    private CandidateService candidateService;
    private UserService userService;

    public ElectionsService(VoteService voteService, ElectionProperties electionProps,
            CandidateService candidateService, UserService userService) {
        this.voteService = voteService;
        this.electionProps = electionProps;
        this.candidateService = candidateService;
        this.userService = userService;
    }

    public boolean getElectionCampaignStatus() {
        return null != electionProps.getEnabled() && electionProps.getEnabled();
    }

    public boolean switchElectionCampaignStatus() {
        try {
            electionProps.setEnabled(!electionProps.getEnabled());

            return true;
        } catch (Exception e) {
            log.error("Exception while switching election campaign status: {}", e.getMessage());
        }

        return false;
    }

    public CandidateDTO getElectionResult() {
        List<VoteDTO> allVotes = voteService.getAll();

        Map<Integer, Long> candidatesAndVotes = allVotes.stream()
                .collect(Collectors.groupingBy(VoteDTO::getCandidateID, Collectors.counting()));

        Integer winnerCandidate = Collections.max(candidatesAndVotes.entrySet(), Map.Entry.comparingByValue()).getKey();

        return candidateService.get(winnerCandidate);
    }

    public boolean vote(CandidateDTO voted, Integer userID) {
        VoteDTO newVote = new VoteDTO();

        newVote.setCandidateID(voted.getId());
        newVote.setParty(voted.getParty());
        newVote.setTimestamp(System.currentTimeMillis());

        // save the voting action on user entity
        UserDTO userToVote = userService.get(userID);

        if (null == userToVote || null == userToVote.getId()) {
            return false; // no user found in DB
        }

        if (null != userToVote && null != userToVote.getId() && null != userToVote.getHasVoted()
                && userToVote.getHasVoted()) {
            return false; // user has voted
        }

        userToVote.setHasVoted(true);

        userService.save(userToVote);

        return voteService.takeAVote(newVote);
    }

    public List<CandidateWithStatisticsDTO> getParsedVotes() {
        List<CandidateWithStatisticsDTO> returnable = new ArrayList<>();

        List<VoteDTO> votes = voteService.getAll();
        List<CandidateDTO> candidates = candidateService.getAll();

        candidates.forEach(candidate -> {
            var candidateVotes = votes.stream().map(VoteDTO::getCandidateID)
                    .filter(candidateID -> candidateID.equals(candidate.getId())).count();

            var newCandidateWithStatistics = new CandidateWithStatisticsDTO();

            newCandidateWithStatistics.setId(candidate.getId());
            newCandidateWithStatistics.setFirstName(candidate.getFirstName());
            newCandidateWithStatistics.setLastName(candidate.getLastName());
            newCandidateWithStatistics.setDescription(candidate.getDescription());
            newCandidateWithStatistics.setImage(candidate.getImage());
            newCandidateWithStatistics.setParty(candidate.getParty());
            newCandidateWithStatistics.setTotalVotes(candidateVotes);

            returnable.add(newCandidateWithStatistics);
        });

        return returnable;
    }

    public boolean hasUserVoted(String username) {
        return userService.hasVotedByUsername(username);
    }

    public int countAllVotes() {
        return voteService.getAll().size();
    }

    // XXX: dangerous method, use with care
    public boolean cleanAllVotes() {
        return voteService.cleanDBTable();
    }
}
