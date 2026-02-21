package ro.cristiansterie.vote.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ro.cristiansterie.vote.dto.DashboardTotalsDTO;
import ro.cristiansterie.vote.service.DashboardService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class DashboardControllerTest extends GenericControllerTest {

    @MockBean
    private DashboardService service;

    // ---------------------------------------------------------
    // GET /dashboard/totals/{electionId}
    // ---------------------------------------------------------
    @Test
    void totals_returns200WhenTotalsExists() throws Exception {
        DashboardTotalsDTO totals = new DashboardTotalsDTO();
        totals.setCandidates(10);
        totals.setUsers(100);

        when(service.getTotals(1L)).thenReturn(totals);

        mockMvc.perform(get("/dashboard/totals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.candidates").value(10))
                .andExpect(jsonPath("$.users").value(100));

        verify(service).getTotals(1L);
    }

    @Test
    void totals_returns400WhenTotalsIsNull() throws Exception {
        when(service.getTotals(1L)).thenReturn(null);

        mockMvc.perform(get("/dashboard/totals/1"))
                .andExpect(status().isBadRequest());

        verify(service).getTotals(1L);
    }
}
