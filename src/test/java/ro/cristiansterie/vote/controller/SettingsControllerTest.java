package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ro.cristiansterie.vote.service.SettingsService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class SettingsControllerTest extends GenericControllerTest {

    @MockBean
    private SettingsService service;

    // ---------------------------------------------------------
    // POST /settings/fake/candidates/{electionId}
    // ---------------------------------------------------------
    @Test
    void fakeCandidates_returns200WhenStatusTrue() throws Exception {
        when(service.generateFakeCandidates(anyLong())).thenReturn(true);

        mockMvc.perform(post("/settings/fake/candidates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).generateFakeCandidates(anyLong());
    }

    @Test
    void fakeCandidates_returns400WhenStatusFalse() throws Exception {
        when(service.generateFakeCandidates(anyLong())).thenReturn(false);

        mockMvc.perform(post("/settings/fake/candidates/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).generateFakeCandidates(anyLong());
    }

    // ---------------------------------------------------------
    // POST /settings/fake/users/{no}
    // ---------------------------------------------------------
    @Test
    void fakeUsers_returns200WhenStatusTrue() throws Exception {
        when(service.generateFakeUsers(anyInt())).thenReturn(true);

        mockMvc.perform(post("/settings/fake/users/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).generateFakeUsers(anyInt());
    }

    @Test
    void fakeUsers_returns400WhenStatusFalse() throws Exception {
        when(service.generateFakeUsers(anyInt())).thenReturn(false);

        mockMvc.perform(post("/settings/fake/users/10"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).generateFakeUsers(anyInt());
    }

    // ---------------------------------------------------------
    // POST /settings/fake/votes/{no}/{electionId}
    // ---------------------------------------------------------
    @Test
    void fakeVotes_returns200WhenStatusNotNull() throws Exception {
        when(service.generateFakeVotes(anyInt(), anyLong())).thenReturn(true);

        mockMvc.perform(post("/settings/fake/votes/5/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).generateFakeVotes(anyInt(), anyLong());
    }

    @Test
    void fakeVotes_returns400WhenStatusIsNull() throws Exception {
        when(service.generateFakeVotes(anyInt(), anyLong())).thenReturn(null);

        mockMvc.perform(post("/settings/fake/votes/5/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).generateFakeVotes(anyInt(), anyLong());
    }
}
