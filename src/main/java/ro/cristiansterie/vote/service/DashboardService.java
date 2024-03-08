package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.DashboardTotalsDTO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;

@Service
public class DashboardService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CandidateRepository candidates;
    private UserRepository users;

    public DashboardService(CandidateRepository candidates, UserRepository users) {
        this.candidates = candidates;
        this.users = users;
    }

    public DashboardTotalsDTO getTotals() {
        DashboardTotalsDTO totals = new DashboardTotalsDTO();

        totals.setCandidates((int) candidates.count());
        totals.setUsers((int) users.count());

        return totals;
    }
}
