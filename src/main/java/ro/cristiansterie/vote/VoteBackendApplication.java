package ro.cristiansterie.vote;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
			log.info("Application started successfully! message from the CommandLineRunner ;)");
		};
	}

}
