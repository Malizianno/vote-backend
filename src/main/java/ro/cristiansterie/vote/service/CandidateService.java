package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import ro.cristiansterie.vote.aspect.Loggable;
import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateFilterDTO;
import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.PartyTypeEnum;

@Service
public class CandidateService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static final String CANDIDATES_SCREEN = AppConstants.EVENT_SCREEN_CANDIDATES;

    private final CandidateRepository repo;

    public CandidateService(CandidateRepository repo) {
        this.repo = repo;
    }

    public List<CandidateDTO> getAll() {
        return convert(repo.findAll());
    }

    public List<CandidateDTO> getAllForElection(long id) {
        return convert(repo.findAllByElectionId(id));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_FILTERED, screen = CANDIDATES_SCREEN, message = AppConstants.EVENT_CANDIDATES_GET_FILTERED)
    public List<CandidateDTO> getFiltered(CandidateFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Pageable pageable = null;
        if (filter.getPaging() != null) {
            pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                    Sort.by(Sort.Direction.DESC, "id"));
        }

        // return filtered candidates
        List<CandidateDAO> candidatesToReturn;
        if (filter.getPaging() == null || pageable == null) {
            candidatesToReturn = repo.findAll(Example.of(convert(filter.getObject()), matcher));
        } else {
            candidatesToReturn = repo.findAll(Example.of(convert(filter.getObject()), matcher), pageable)
                    .getContent();
        }

        return convert(candidatesToReturn);
    }

    @Loggable(action = AppConstants.EVENT_ACTION_COUNT_FILTERED, screen = CANDIDATES_SCREEN, message = AppConstants.EVENT_CANDIDATES_COUNT_FILTERED)
    public Long countFiltered(CandidateFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return repo.count(Example.of(convert(filter.getObject()), matcher));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ONE, screen = CANDIDATES_SCREEN, message = AppConstants.EVENT_CANDIDATES_GET_ONE)
    public CandidateDTO get(@NonNull Long id) {
        CandidateDAO returnable = repo.findById(id).orElse(null);

        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_SAVE, screen = CANDIDATES_SCREEN, message = AppConstants.EVENT_CANDIDATES_SAVE)
    public CandidateDTO save(CandidateDTO toSave) {
        var saved = repo.save(convert(toSave));

        return convert(saved);
    }

    @Loggable(action = AppConstants.EVENT_ACTION_DELETE, screen = CANDIDATES_SCREEN, message = AppConstants.EVENT_CANDIDATES_DELETE)
    public Boolean delete(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Cannot delete candidate {} because: {}", id, e);
        }

        return false;
    }

    // CONVERTERS

    protected CandidateDAO convert(CandidateDTO candidate) {
        return mapper.map(candidate, CandidateDAO.class);
    }

    protected CandidateDTO convert(CandidateDAO candidate) {
        return mapper.map(candidate, CandidateDTO.class);
    }

    protected List<CandidateDTO> convert(List<CandidateDAO> candidates) {
        return candidates.stream().map(this::convert).collect(Collectors.toList());
    }

    private CandidateFilterDTO checkFilters(CandidateFilterDTO filter) {
        if (filter.getObject() == null) {
            filter.setObject(new CandidateDTO());
        }

        if (PartyTypeEnum.ALL.equals(filter.getObject().getParty())) {
            filter.getObject().setParty(null);
        }

        return filter;
    }
}
