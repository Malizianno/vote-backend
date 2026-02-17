package ro.cristiansterie.vote.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.entity.NewsfeedPostDAO;
import ro.cristiansterie.vote.repository.NewsfeedRepository;

@Service
public class NewsfeedService extends GenericService {

    private final NewsfeedRepository repo;

    public NewsfeedService(NewsfeedRepository repo) {
        this.repo = repo;
    }

    public List<NewsfeedPostDTO> findAll() {
        return convert(repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public NewsfeedPostDTO findById(Long id) {
        return convert(repo.findById(id).orElse(null));
    }

    public NewsfeedPostDTO create(NewsfeedPostDAO post) {
        var context = SecurityContextHolder.getContext();
        var user = context.getAuthentication().getName();
        post.setCreatedBy(user);
        
        return convert(repo.save(post));
    }

    public NewsfeedPostDTO update(Long id, NewsfeedPostDAO updated) {
        NewsfeedPostDAO existing = repo.findById(id).orElse(updated);

        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        existing.setImageUrl(updated.getImageUrl());

        return convert(repo.save(existing));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public NewsfeedPostDTO convert(NewsfeedPostDAO dao) {
        return mapper.map(dao, NewsfeedPostDTO.class);
    }

    public NewsfeedPostDAO convert(NewsfeedPostDTO dto) {
        return mapper.map(dto, NewsfeedPostDAO.class);
    }

    public List<NewsfeedPostDTO> convert(List<NewsfeedPostDAO> daos) {
        return daos.stream().map(this::convert).toList();
    }
}
