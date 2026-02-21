package ro.cristiansterie.vote;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import ro.cristiansterie.vote.properties.AuthProperties;
import ro.cristiansterie.vote.properties.JWTConfiguration;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;

@SpringBootApplication
@EnableConfigurationProperties({ JWTConfiguration.class, AuthProperties.class })
@EnableScheduling
public class VoteBackendApplication {
	protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		SpringApplication.run(VoteBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepo, CandidateRepository candidateRepo) {
		return args -> {
			log.info("Application started successfully! message from the CommandLineRunner ;)");

			// WIP: TEST DATA TO BE DELETED!!!
			var faceImage3 = new String(VoteBackendApplication.class
					.getResourceAsStream("/base64test/test-face-image-3.b64").readAllBytes(), StandardCharsets.UTF_8);
			var idImage3 = new String(
					VoteBackendApplication.class.getResourceAsStream("/base64test/test-id-image-3.b64").readAllBytes(),
					StandardCharsets.UTF_8);
			var faceImage4 = new String(VoteBackendApplication.class
					.getResourceAsStream("/base64test/test-face-image-4.b64").readAllBytes(), StandardCharsets.UTF_8);
			var idImage4 = new String(
					VoteBackendApplication.class.getResourceAsStream("/base64test/test-id-image-4.b64").readAllBytes(),
					StandardCharsets.UTF_8);

			var user3 = userRepo.findById(3L).get();
			var user4 = userRepo.findById(4L).get();

			user3.setFaceImage(Base64.getDecoder().decode(faceImage3));
			user3.setIdImage(Base64.getDecoder().decode(idImage3));
			user4.setFaceImage(Base64.getDecoder().decode(faceImage4));
			user4.setIdImage(Base64.getDecoder().decode(idImage4));

			userRepo.saveAllAndFlush(Arrays.asList(user3, user4));
		};
	}
}
