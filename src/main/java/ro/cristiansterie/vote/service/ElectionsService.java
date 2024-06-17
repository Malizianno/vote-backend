package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.CandidateDTO;
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

    public boolean vote(CandidateDTO voted) {
        VoteDTO newVote = new VoteDTO();

        newVote.setCandidateID(voted.getId());
        newVote.setParty(voted.getParty());
        newVote.setTimestamp(System.currentTimeMillis());

        return voteService.takeAVote(newVote);
    }

    public boolean hasUserVoted(String username) {
        return userService.hasVotedByUsername(username);
    }

    // XXX: dangerous method, use with care
    public boolean cleanAllVotes() {
        return voteService.cleanDBTable();
    }
}
