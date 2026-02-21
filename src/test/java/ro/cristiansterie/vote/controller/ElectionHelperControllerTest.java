package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.dto.ElectionCampaignDTO;
import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.service.ElectionsHelperService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class ElectionHelperControllerTest extends GenericControllerTest {

    @MockBean
    private ElectionsHelperService service;

    // ---------------------------------------------------------
    // GET /election/helper/status
    // ---------------------------------------------------------
    @Test
    void status_returns200WhenStatusExists() throws Exception {
        ElectionCampaignDTO dto = new ElectionCampaignDTO();
        dto.setEnabled(true);
        dto.setElections(List.of(new ElectionDTO()));

        when(service.getElectionCampaignStatus()).thenReturn(dto);

        mockMvc.perform(get("/election/helper/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.elections.length()").value(1));

        verify(service, atLeastOnce()).getElectionCampaignStatus();
    }

    @Test
    void status_returns400WhenStatusIsNull() throws Exception {
        when(service.getElectionCampaignStatus()).thenReturn(null);

        mockMvc.perform(get("/election/helper/status"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getElectionCampaignStatus();
    }

    // ---------------------------------------------------------
    // GET /election/helper/result/{electionId}
    // ---------------------------------------------------------
    @Test
    void result_returns200WhenResultExists() throws Exception {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(1L);
        dto.setElectionId(1L);

        when(service.getElectionResult(1L)).thenReturn(dto);

        mockMvc.perform(get("/election/helper/result/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.electionId").value(1L));

        verify(service, times(1)).getElectionResult(1L);
    }

    @Test
    void result_returns400WhenResultIsNull() throws Exception {
        when(service.getElectionResult(1L)).thenReturn(null);

        mockMvc.perform(get("/election/helper/result/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getElectionResult(1L);
    }

    // ---------------------------------------------------------
    // GET /election/helper/cleanDB/{electionId}
    // ---------------------------------------------------------
    @Test
    void cleanDB_returns200WhenEntitiesExists() throws Exception {
        when(service.cleanAllVotes(1)).thenReturn(true);

        mockMvc.perform(get("/election/helper/cleanDB/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).cleanAllVotes(1);
    }

    @Test
    void cleanDB_returns400WhenEntitiesAreNull() throws Exception {
        when(service.cleanAllVotes(1)).thenReturn(null);

        mockMvc.perform(get("/election/helper/cleanDB/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).cleanAllVotes(1);
    }

    // ---------------------------------------------------------
    // POST /election/helper/vote
    // ---------------------------------------------------------
    @Test
    void vote_returns200WhenVotingSucceeds() throws Exception {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(1L);
        dto.setElectionId(1L);

        when(service.vote(any(CandidateDTO.class), anyLong())).thenReturn(true);

        mockMvc.perform(post("/election/helper/vote")
                .param("userID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).vote(any(CandidateDTO.class), anyLong());
    }

    @Test
    void vote_returns400WhenVotingFails() throws Exception {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(1L);
        dto.setElectionId(1L);

        when(service.vote(any(CandidateDTO.class), anyLong())).thenReturn(null);

        mockMvc.perform(post("/election/helper/vote")
                .param("userID", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).vote(any(CandidateDTO.class), anyLong());
    }

    // ---------------------------------------------------------
    // GET /election/helper/countAllVotes/{electionId}
    // ---------------------------------------------------------
    @Test
    void countAllVotes_returns200WhenResultExists() throws Exception {
        when(service.countAllVotes(anyLong())).thenReturn(10L);

        mockMvc.perform(get("/election/helper/countAllVotes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10L));

        verify(service, times(1)).countAllVotes(anyLong());
    }

    @Test
    void countAllVotes_returns400WhenResultIsNull() throws Exception {
        when(service.countAllVotes(anyLong())).thenReturn(null);

        mockMvc.perform(get("/election/helper/countAllVotes/0"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).countAllVotes(anyLong());
    }

    // ---------------------------------------------------------
    // GET /election/helper/getParsedVotes/{electionId}
    // ---------------------------------------------------------
    @Test
    void getParsedVotes_returns200WhenResultExists() throws Exception {
        CandidateWithStatisticsDTO candidate1 = new CandidateWithStatisticsDTO();
        candidate1.setId(1L);
        candidate1.setElectionId(1L);
        candidate1.setTotalVotes(10L);

        CandidateWithStatisticsDTO candidate2 = new CandidateWithStatisticsDTO();
        candidate2.setId(2L);
        candidate2.setElectionId(1L);
        candidate2.setTotalVotes(10L);

        when(service.getParsedVotes(anyLong())).thenReturn(List.of(candidate1, candidate2));

        mockMvc.perform(get("/election/helper/getParsedVotes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(service, times(1)).getParsedVotes(anyLong());
    }

    @Test
    void getParsedVotes_returns400WhenResultIsNull() throws Exception {
        when(service.getParsedVotes(anyLong())).thenReturn(null);

        mockMvc.perform(get("/election/helper/getParsedVotes/0"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getParsedVotes(anyLong());
    }
}
