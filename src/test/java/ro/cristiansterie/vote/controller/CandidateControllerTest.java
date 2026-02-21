package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateFilterDTO;
import ro.cristiansterie.vote.dto.CandidateResponseDTO;
import ro.cristiansterie.vote.service.CandidateService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class CandidateControllerTest extends GenericControllerTest {

    @MockBean
    private CandidateService candidateService;

    // ---------------------------------------------------------
    // GET /candidates/{id}
    // ---------------------------------------------------------
    @Test
    void one_returns200WhenCandidateExists() throws Exception {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(1L);
        dto.setFirstName("Alice");

        when(candidateService.get(1L)).thenReturn(dto);

        mockMvc.perform(get("/candidates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"));

        verify(candidateService).get(1L);
    }

    @Test
    void one_returns400WhenCandidateIsNull() throws Exception {
        when(candidateService.get(1L)).thenReturn(null);

        mockMvc.perform(get("/candidates/1"))
                .andExpect(status().isBadRequest());

        verify(candidateService).get(1L);
    }

    // ---------------------------------------------------------
    // POST /candidates/filtered
    // ---------------------------------------------------------
    @Test
    void filtered_returns200WithCandidates() throws Exception {
        CandidateDTO c1 = new CandidateDTO();
        c1.setId(1L);
        c1.setFirstName("Alice");

        CandidateResponseDTO response = new CandidateResponseDTO();
        response.setCandidates(List.of(c1));
        response.setTotal(1L);

        when(candidateService.getFiltered(any(CandidateFilterDTO.class)))
                .thenReturn(List.of(c1));
        when(candidateService.countFiltered(any(CandidateFilterDTO.class)))
                .thenReturn(1L);

        mockMvc.perform(post("/candidates/filtered")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "candidate": { "firstName": "Alice" }
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.candidates.length()").value(1))
                .andExpect(jsonPath("$.total").value(1));

        verify(candidateService).getFiltered(any(CandidateFilterDTO.class));
        verify(candidateService).countFiltered(any(CandidateFilterDTO.class));
    }

    @Test
    void filtered_returns400WhenCandidatesNull() throws Exception {
        when(candidateService.getFiltered(any(CandidateFilterDTO.class)))
                .thenReturn(null);

        mockMvc.perform(post("/candidates/filtered")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "candidate": { "firstName": "Alice" }
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------
    // GET /candidates/allForElection/{id}
    // ---------------------------------------------------------
    @Test
    void allForElection_returns200WithList() throws Exception {
        CandidateDTO c1 = new CandidateDTO();
        c1.setId(1L);

        when(candidateService.getAllForElection(10))
                .thenReturn(List.of(c1));

        mockMvc.perform(get("/candidates/allForElection/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(candidateService).getAllForElection(10);
    }

    @Test
    void allForElection_returns400WhenNull() throws Exception {
        when(candidateService.getAllForElection(10))
                .thenReturn(null);

        mockMvc.perform(get("/candidates/allForElection/10"))
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------
    // POST /candidates/add
    // ---------------------------------------------------------
    @Test
    void add_returns200WhenSaved() throws Exception {
        CandidateDTO request = new CandidateDTO();
        request.setFirstName("Alice");

        CandidateDTO saved = new CandidateDTO();
        saved.setId(1L);
        saved.setFirstName("Alice");

        when(candidateService.save(any(CandidateDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/candidates/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "firstName": "Alice"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"));

        verify(candidateService).save(any(CandidateDTO.class));
    }

    @Test
    void add_returns400WhenServiceReturnsNull() throws Exception {
        when(candidateService.save(any(CandidateDTO.class))).thenReturn(null);

        mockMvc.perform(post("/candidates/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "firstName": "Alice"
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------
    // DELETE /candidates/delete/{id}
    // ---------------------------------------------------------
    @Test
    void delete_returns200WhenDeleted() throws Exception {
        when(candidateService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/candidates/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(candidateService).delete(1L);
    }

    @Test
    void delete_returns400WhenServiceReturnsNull() throws Exception {
        when(candidateService.delete(1L)).thenReturn(null);

        mockMvc.perform(delete("/candidates/delete/1"))
                .andExpect(status().isBadRequest());
    }
}
