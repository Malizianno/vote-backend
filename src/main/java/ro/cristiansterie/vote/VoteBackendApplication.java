package ro.cristiansterie.vote;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;

@SpringBootApplication
public class VoteBackendApplication {
	protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		SpringApplication.run(VoteBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepo, CandidateRepository candidateRepo) {
		return args -> {
			UserDAO user = new UserDAO();
			user.setUsername("test-user-username");

			UserDAO saved = userRepo.save(user);

			log.info("user {} saved...", saved.getUsername());

			CandidateDAO candidate1 = new CandidateDAO();
			candidate1.setFirstName("first name");
			candidate1.setLastName("last name");
			candidate1.setParty("PSD");
			candidate1.setImage("http://image.com/#");
			candidate1.setDescription("I am RED :))) PSD joke");

			CandidateDAO savedCandidate = candidateRepo.save(candidate1);

			log.info("candidate {} saved...", savedCandidate.getFirstName() + savedCandidate.getLastName());
		};
	}

}
