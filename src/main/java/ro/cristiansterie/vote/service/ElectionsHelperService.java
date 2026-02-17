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
import org.springframework.transaction.annotation.Transactional;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateFilterDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.dto.ElectionCampaignDTO;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.VoteDTO;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;

// XXX: this class executes elections, take care of voting process alltogether
@Service
public class ElectionsHelperService extends GenericService {

    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private VoteService voteService;
    private CandidateService candidateService;
    private UserService userService;
    private EventService eventService;
    private final ElectionService electionService;

    public ElectionsHelperService(VoteService voteService, CandidateService candidateService, UserService userService,
            EventService eventService, ElectionService electionService) {
        this.voteService = voteService;
        this.candidateService = candidateService;
        this.userService = userService;
        this.eventService = eventService;
        this.electionService = electionService;
    }

    public ElectionCampaignDTO getElectionCampaignStatus() {
        ElectionCampaignDTO returnable = new ElectionCampaignDTO();

        var campaign = electionService.getAll().stream().filter(el -> el.getEnabled()).collect(Collectors.toList());

        returnable.setEnabled(campaign.size() > 0);
        returnable.setElections(campaign);

        // save event
        eventService.save(EventActionEnum.GET, EventScreenEnum.ELECTIONS_HELPER,
                AppConstants.EVENT_ELECTIONS_HELPER_GET_CAMPAIGN_STATUS);

        return returnable;
    }

    public CandidateDTO getElectionResult(long electionId) {
        VoteDTO filterVote = new VoteDTO();
        filterVote.setElectionId(electionId);
        List<VoteDTO> allVotes = voteService.getFiltered(filterVote);

        Map<Long, Long> candidatesAndVotes = allVotes.stream()
                .collect(Collectors.groupingBy(VoteDTO::getCandidateID, Collectors.counting()));

        Long winnerCandidate = Collections.max(candidatesAndVotes.entrySet(), Map.Entry.comparingByValue()).getKey();

        // save event
        eventService.save(EventActionEnum.GET, EventScreenEnum.ELECTIONS_HELPER,
                AppConstants.EVENT_ELECTIONS_HELPER_GET_ELECTION_RESULT);

        return candidateService.get(winnerCandidate);
    }

    @Transactional
    public boolean vote(CandidateDTO voted, Long userID) {
        VoteDTO newVote = new VoteDTO();

        newVote.setCandidateID(voted.getId());
        newVote.setParty(voted.getParty());
        newVote.setTimestamp(System.currentTimeMillis());
        newVote.setElectionId(voted.getElectionId());

        // save the voting action on user entity
        UserDTO userToVote = userService.get(userID);

        if (null == userToVote || null == userToVote.getId()) {
            return false; // no user found in DB
        }

        if (null != userToVote && null != userToVote.getId() && null != userToVote.getHasVoted()
                && userToVote.getHasVoted()) {
            return false; // user has voted
        }

        // TODO: check if this really made it work for mobile :)
        // userToVote.setHasVoted(true);
        // userService.save(userToVote);

        // save event
        eventService.save(EventActionEnum.CREATE, EventScreenEnum.ELECTIONS_HELPER,
                AppConstants.EVENT_ELECTIONS_HELPER_VOTE + voted.getId());

        return voteService.takeAVote(newVote);
    }

    public List<CandidateWithStatisticsDTO> getParsedVotes(long electionId) {
        List<CandidateWithStatisticsDTO> returnable = new ArrayList<>();

        VoteDTO filterVote = new VoteDTO();
        filterVote.setElectionId(electionId);

        CandidateFilterDTO filterCandidate = new CandidateFilterDTO();
        CandidateDTO candidateUsedForFilter = new CandidateDTO();
        candidateUsedForFilter.setElectionId(electionId);
        filterCandidate.setObject(candidateUsedForFilter);

        List<VoteDTO> votes = voteService.getFiltered(filterVote);
        List<CandidateDTO> candidates = candidateService.getFiltered(filterCandidate);

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

        // save event
        eventService.save(EventActionEnum.GET, EventScreenEnum.ELECTIONS_HELPER,
                AppConstants.EVENT_ELECTIONS_HELPER_GET_PARSED_VOTES);

        return returnable;
    }

    public boolean hasUserVoted(String username) {
        return userService.hasVotedByUsername(username);
    }

    public long countAllVotes(long electionId) {
        VoteDTO filterVote = new VoteDTO();
        filterVote.setElectionId(electionId);

        return voteService.getFiltered(filterVote).size();
    }

    // XXX: dangerous method, use with care
    public boolean cleanAllVotes(int electionId) {
        // save event
        eventService.save(EventActionEnum.DELETE, EventScreenEnum.ELECTIONS_HELPER,
                AppConstants.EVENT_ELECTIONS_HELPER_CLEAN_ALL_VOTES);

        return voteService.cleanDBTable(electionId);
    }
}
