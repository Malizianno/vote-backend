package ro.cristiansterie.vote;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ro.cristiansterie.vote.properties.AuthProperties;
import ro.cristiansterie.vote.properties.ElectionProperties;
import ro.cristiansterie.vote.properties.JWTConfiguration;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;

@SpringBootApplication
@EnableConfigurationProperties({ JWTConfiguration.class, AuthProperties.class, ElectionProperties.class })
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

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}
}
