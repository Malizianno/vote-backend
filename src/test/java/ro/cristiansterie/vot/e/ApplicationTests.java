package ro.cristiansterie.vot.e;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ro.cristiansterie.vote.VoteBackendApplication;

@SpringBootTest(classes = VoteBackendApplication.class)
@ActiveProfiles("test")
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
