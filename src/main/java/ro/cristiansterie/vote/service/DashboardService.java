package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.security.Security;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.cristiansterie.vote.dto.DashboardTotalsDTO;
import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EntityHelper;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;

@Service
public class DashboardService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CandidateRepository candidates;
    private UserRepository users;

    private CandidateService candidatesService;
    private ElectionsHelperService electionHelperService;
    private ElectionService electionService;
    private EventService events;

    public DashboardService(CandidateRepository candidates, UserRepository users, CandidateService candidatesService,
            ElectionsHelperService electionsHelperService, ElectionService electionService, EventService events) {
        this.candidates = candidates;
        this.users = users;
        this.candidatesService = candidatesService;
        this.electionHelperService = electionsHelperService;
        this.electionService = electionService;
        this.events = events;
    }

    public DashboardTotalsDTO getTotals() {
        DashboardTotalsDTO totals = new DashboardTotalsDTO();

        totals.setCandidates((int) candidates.count());
        totals.setUsers((int) users.count());

        // save event
        events.save(EventActionEnum.GET_ALL, EventScreenEnum.DASHBOARD, AppConstants.EVENT_DASHBOARD_GET_TOTALS);

        return totals;
    }

    public boolean generateFakeUsers(int no) {
        try {
            // save event
            events.save(EventActionEnum.CREATE, EventScreenEnum.DASHBOARD,
                    AppConstants.EVENT_DASHBOARD_GENERATE_FAKE_USERS);

            users.saveAll(EntityHelper.generateFakeUsers(no));

            return true;
        } catch (Exception e) {
            log.info("Cannot save fake users because of: {}", e.getMessage());
        }

        return false;
    }

    public boolean generateFakeCandidates() {
        try {
            // first clean the DB with previous candidates
            candidates.deleteAll();
            // generate fake candidates
            var fakeCandidates = EntityHelper.generateFakeCandidates();
            // then generate and save new fake candidates
            candidates.saveAll(fakeCandidates);

            // add candidates to live election
            var election = electionService.getLastElection();

            // save event
            events.save(EventActionEnum.CREATE, EventScreenEnum.DASHBOARD,
                    AppConstants.EVENT_DASHBOARD_GENERATE_FAKE_CANDIDATES);

            if (election == null || election.getEndDate() != null)
                throw new IllegalStateException("No election to add fake candidates to!");

            election.setCandidates(candidatesService.convert(fakeCandidates));
            electionService.save(election);

            return true;
        } catch (Exception e) {
            log.info("Cannot save fake candidates because of: {}", e.getMessage());
        }

        return false;
    }

    // XXX: this is very danbgerous, please REMOVEW THIS method after testing
    // and quit generating fake votes
    @Transactional
    public boolean generateFakeVotes(int no) {
        try {
            List<CandidateDAO> candidatesList = candidates.findAll();
            List<UserDAO> usersList = users.findAll();
            int maxRand = no / candidatesList.size(); // votes per candidate

            // testing
            var originalAuth = SecurityContextHolder.getContext().getAuthentication();

            int totalVotesAfterRand = 0;
            for (CandidateDAO candidate : candidatesList) {
                int randVotesNo = new Random().nextInt(maxRand);

                for (int i = 0; i < randVotesNo; i++) {
                    var user = usersList.get(totalVotesAfterRand + i);

                    AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken(
                            "key123", // unique key (can be any string)
                            user.getUsername(), // principal name
                            AuthorityUtils.createAuthorityList("ADMIN") // granted authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(anonymousToken);

                    electionHelperService.vote(candidatesService.convert(candidate),
                            user.getId());
                }

                totalVotesAfterRand += randVotesNo;
            }

            // end testing
            // restore original authentication
            SecurityContextHolder.getContext().setAuthentication(originalAuth);

            // save event
            events.save(EventActionEnum.CREATE, EventScreenEnum.DASHBOARD,
                    AppConstants.EVENT_DASHBOARD_GENERATE_FAKE_VOTES);

            return true;
        } catch (Exception e) {
            log.info("Cannot generate fake votes because of: {}", e.getMessage());
        }

        return false;
    }
}
