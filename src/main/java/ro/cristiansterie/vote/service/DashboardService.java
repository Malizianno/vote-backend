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
import ro.cristiansterie.vote.util.EntityHelper;

@Service
public class DashboardService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CandidateRepository candidates;
    private UserRepository users;

    private CandidateService candidatesService;
    private ElectionsService electionsService;

    public DashboardService(CandidateRepository candidates, UserRepository users, CandidateService candidatesService,
            ElectionsService electionsService) {
        this.candidates = candidates;
        this.users = users;
        this.candidatesService = candidatesService;
        this.electionsService = electionsService;
    }

    public DashboardTotalsDTO getTotals() {
        DashboardTotalsDTO totals = new DashboardTotalsDTO();

        totals.setCandidates((int) candidates.count());
        totals.setUsers((int) users.count());

        return totals;
    }

    public boolean generateFakeUsers(int no) {
        try {
            users.saveAll(EntityHelper.generateFakeUsers(no));

            return true;
        } catch (Exception e) {
            log.info("Cannot save fake users because of: {}", e.getMessage());
        }

        return false;
    }

    public boolean generateFakeCandidates() {
        try {
            candidates.saveAll(EntityHelper.generateFakeCandidates());

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
                int randVotesNo = new Random().nextInt(maxRand);

                for (int i = 0; i < randVotesNo; i++) {
                    electionsService.vote(candidatesService.convert(candidate), 999);

                    UserDAO user = users.findById(999).get();
                    user.setHasVoted(false);
                    users.save(user);
                }
            });

            return true;
        } catch (Exception e) {
            log.info("Cannot generate fake votes because of: {}", e.getMessage());
        }

        return false;
    }
}
