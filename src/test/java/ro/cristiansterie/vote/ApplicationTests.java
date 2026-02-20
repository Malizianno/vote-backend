package ro.cristiansterie.vote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoteBackendApplication.class)
@ActiveProfiles("test")
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
