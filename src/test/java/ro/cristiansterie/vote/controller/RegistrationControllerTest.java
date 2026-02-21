package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.service.UserService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class RegistrationControllerTest extends GenericControllerTest {

    @MockBean
    private UserService service;

    // ---------------------------------------------------------
    // POST /register/
    // ---------------------------------------------------------
    @Test
    void registerProfile_returns200WhenRegistered() throws Exception {
        UserVoterDTO request = new UserVoterDTO();

        UserVoterDTO registered = new UserVoterDTO ();
        registered.setId(1L);

        when(service.registerProfile(any())).thenReturn(registered);

        mockMvc.perform(
                post("/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).registerProfile(any());
    }

    @Test
    void registerProfile_returns400WhenRegistrationFails() throws Exception {
        UserVoterDTO request = new UserVoterDTO();

        when(service.registerProfile(any())).thenReturn(null);

        mockMvc.perform(
                post("/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).registerProfile(any());
    }
}
