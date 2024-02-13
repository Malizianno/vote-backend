package ro.cristiansterie.vote;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.UserRepository;

@SpringBootApplication
// @EnableJpaRepositories("ro.cristiansterie.vote.repository")
@EntityScan("ro.cristiansterie.vote.entity")
public class VoteBackendApplication {
	protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		SpringApplication.run(VoteBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository repo) {
		return args -> {
			UserDAO user = new UserDAO();
			user.setUsername("test-user-username");

			UserDAO saved = repo.save(user);

			log.info("user {} saved...", saved.getUsername());
		};
	}

}
