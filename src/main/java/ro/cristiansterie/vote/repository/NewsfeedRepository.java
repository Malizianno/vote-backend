package ro.cristiansterie.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.NewsfeedPostDAO;

public interface NewsfeedRepository extends JpaRepository<NewsfeedPostDAO, Long> {

}
