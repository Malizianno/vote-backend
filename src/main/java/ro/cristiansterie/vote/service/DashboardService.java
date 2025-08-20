package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public boolean generateFakeVotes(int no) {
        try {
            List<CandidateDAO> candidatesList = candidates.findAll();
            int maxRand = no / candidatesList.size();

            candidatesList.forEach(candidate -> {
                int randVotesNo = new Random().nextInt(maxRand > 0 ? maxRand : 1);

                for (int i = 0; i < randVotesNo; i++) {
                    electionHelperService.vote(candidatesService.convert(candidate), i + 1);

                    UserDAO user = users.findById(i + 1).get();
                    user.setHasVoted(false);
                    users.save(user);
                }
            });

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
