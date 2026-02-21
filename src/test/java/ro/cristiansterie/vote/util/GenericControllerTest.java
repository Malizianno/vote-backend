package ro.cristiansterie.vote.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenericControllerTest {

    @Autowired
    public MockMvc mockMvc;

    public final ObjectMapper mapper;

    public GenericControllerTest() {
        this(new ObjectMapper());
    }

    public GenericControllerTest(ObjectMapper mapper) {
        this.mapper = mapper;
    }

}
