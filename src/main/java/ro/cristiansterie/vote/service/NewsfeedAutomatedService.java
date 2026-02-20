package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class NewsfeedAutomatedService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final static List<Integer> MILESTONES = List.of(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
    private int postedPercentMilestoneReached = 0;

    private final NewsfeedService newsfeedService;
    private final UserService userService;
    private final ElectionsHelperService electionHelperService;

    public NewsfeedAutomatedService(NewsfeedService newsfeedService, UserService userService,
            ElectionsHelperService electionHelperService) {
        this.newsfeedService = newsfeedService;
        this.userService = userService;
        this.electionHelperService = electionHelperService;
    }

    @Scheduled(fixedRate = 600_000) // Check 10 minutes
    public void checkingMilestones() {
        log.info("AUTO::: checking voting status in order to generate automated posts...");

        Predicate<Long> logElection = this::logElectionId;

        getAllElectionIDsActive().stream()
                .filter(logElection)
                .forEach(electionId -> {
                    MILESTONES.stream()
                            .filter(this::checkForMilestone)
                            .filter(milestone -> checkForRealPercentageReached(milestone, electionId))
                            .forEach(milestone -> createAndSaveAutomatedPostForMilestone(milestone, electionId));
                });

        log.info("AUTO::: checking successfully finished.");
    }

    private boolean logElectionId(Long electionId) {
        log.info("AUTO::: Checking election with ID: {}", electionId);

        return true;
    }

    private List<Long> getAllElectionIDsActive() {
        return electionHelperService
                .getElectionCampaignStatus()
                .getElections()
                .stream()
                .map(ElectionDTO::getId)
                .toList();
    }

    private boolean checkForMilestone(int milestonePercent) {
        log.info("AUTO::: Checking for milestone: {}", milestonePercent);
        return postedPercentMilestoneReached < milestonePercent;
    }

    private boolean checkForRealPercentageReached(int milestonePercent, long electionId) {
        UserFilterDTO userFilter = new UserFilterDTO();
        UserDTO user = new UserDTO();
        user.setRole(UserRoleEnum.VOTANT);
        userFilter.setUser(user);

        // XXX: what about per zonecode? as in fiecare judet pentru el???
        var totalVoters = userService.countFiltered(userFilter);
        var allVotes = electionHelperService.countAllVotes(electionId);
        var percentageReal = totalVoters == 0 ? 0 : (allVotes * 100) / totalVoters;

        return percentageReal > milestonePercent;
    }

    private boolean createAndSaveAutomatedPostForMilestone(int percent, long electionId) {
        log.info("AUTO::: Creating automated post for milestone: {}", percent);
        // update the milestone reached, so that we don't post again for the same
        // milestone
        postedPercentMilestoneReached = percent;

        // create and post the newsfeed post for this milestone
        NewsfeedPostDTO post = new NewsfeedPostDTO();

        post.setTitle(AppConstants.AUTOMATED_NEWSFEED_POST_TITLE_PREFIX + percent
                + AppConstants.AUTOMATED_NEWSFEED_POST_TITLE_SUFFFIX);
        post.setContent(AppConstants.AUTOMATED_NEWSFEED_POST_CONTENT);
        post.setImageUrl(AppConstants.AUTOMATED_NEWSFEED_POST_IMAGE_URL);
        post.setElectionId(electionId);

        try {
            newsfeedService.create(post);

            return true;
        } catch (Exception e) {
            System.err.println("Failed to create automated newsfeed post: " + e.getMessage());
        }

        return false;
    }
}
