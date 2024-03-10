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
import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateFilterDTO;
import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.util.PartyTypeEnum;

@Service
public class CandidateService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CandidateRepository repo;

    public CandidateService(CandidateRepository repo) {
        this.repo = repo;
    }

    public List<CandidateDTO> getAll() {
        return convert(repo.findAll());
    }

    public List<CandidateDTO> getFiltered(CandidateFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return convert(repo.findAll(Example.of(convert(filter.getCandidate()), matcher), pageable).getContent());
    }

    public int countFiltered(CandidateFilterDTO filter) {
        filter = this.checkFilters(filter);
        
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return (int) repo.count(Example.of(convert(filter.getCandidate()), matcher));
    }

    public CandidateDTO get(@NonNull Integer id) {
        CandidateDAO returnable = repo.findById(id).orElse(null);
        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    public CandidateDTO save(CandidateDTO toSave) {
        return convert(repo.save(convert(toSave)));
    }

    public boolean delete(Integer id) {
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
        if (PartyTypeEnum.ALL.equals(filter.getCandidate().getParty())) {
            filter.getCandidate().setParty(null);
        }

        return filter;
    }
}
