package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
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

import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.ElectionFilterDTO;
import ro.cristiansterie.vote.service.ElectionService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class ElectionControllerTest extends GenericControllerTest {

    @MockBean
    private ElectionService service;

    // ---------------------------------------------------------
    // GET /election/{id}
    // ---------------------------------------------------------
    @Test
    void one_returns200WhenResultExists() throws Exception {
        ElectionDTO dto = new ElectionDTO();
        dto.setId(1L);
        dto.setCandidates(List.of());

        when(service.get(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/election/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));

        verify(service, times(1)).get(anyLong());
    }

    @Test
    void one_returns400WhenResultIsNull() throws Exception {
        when(service.get(anyLong())).thenReturn(null);

        mockMvc.perform(get("/election/0"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).get(anyLong());
    }

    // ---------------------------------------------------------
    // GET /election/last
    // ---------------------------------------------------------
    @Test
    void getLastActive_returns200() throws Exception {
        ElectionDTO dto = new ElectionDTO();
        dto.setId(5L);

        when(service.getLastActiveElection()).thenReturn(dto);

        mockMvc.perform(get("/election/last"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));

        verify(service, times(1)).getLastActiveElection();
    }

    // ---------------------------------------------------------
    // GET /election/all
    // ---------------------------------------------------------
    @Test
    void all_returns200WhenListExists() throws Exception {
        ElectionDTO dto1 = new ElectionDTO();
        dto1.setId(1L);

        ElectionDTO dto2 = new ElectionDTO();
        dto2.setId(2L);

        when(service.getAll()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/election/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(service, times(1)).getAll();
    }

    @Test
    void all_returns400WhenListIsNull() throws Exception {
        when(service.getAll()).thenReturn(null);

        mockMvc.perform(get("/election/all"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getAll();
    }

    // ---------------------------------------------------------
    // POST /election/filtered
    // ---------------------------------------------------------
    @Test
    void filtered_returns200WhenValid() throws Exception {
        ElectionFilterDTO filter = new ElectionFilterDTO();

        ElectionDTO dto = new ElectionDTO();
        dto.setId(10L);

        when(service.getFiltered(any())).thenReturn(List.of(dto));
        when(service.countFiltered(any())).thenReturn(1L);

        mockMvc.perform(
                post("/election/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elections[0].id").value(10L))
                .andExpect(jsonPath("$.total").value(1L));

        verify(service, times(1)).getFiltered(any());
        verify(service, times(1)).countFiltered(any());
    }

    @Test
    void filtered_returns400WhenListIsNull() throws Exception {
        ElectionFilterDTO filter = new ElectionFilterDTO();

        when(service.getFiltered(any())).thenReturn(null);
        when(service.countFiltered(any())).thenReturn(0L);

        mockMvc.perform(
                post("/election/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getFiltered(any());
        verify(service, times(1)).countFiltered(any());
    }

    // ---------------------------------------------------------
    // POST /election/add
    // ---------------------------------------------------------
    @Test
    void add_returns200WhenSaved() throws Exception {
        ElectionDTO toSave = new ElectionDTO();
        toSave.setId(0L);

        ElectionDTO saved = new ElectionDTO();
        saved.setId(100L);

        when(service.save(any())).thenReturn(saved);

        mockMvc.perform(
                post("/election/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L));

        verify(service, times(1)).save(any());
    }

    @Test
    void add_returns400WhenSaveFails() throws Exception {
        ElectionDTO toSave = new ElectionDTO();

        when(service.save(any())).thenReturn(null);

        mockMvc.perform(
                post("/election/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).save(any());
    }

    // ---------------------------------------------------------
    // DELETE /election/delete/{id}
    // ---------------------------------------------------------
    @Test
    void delete_returns200WhenDeleted() throws Exception {
        when(service.delete(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/election/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).delete(anyLong());
    }

    @Test
    void delete_returns400WhenDeleteFails() throws Exception {
        when(service.delete(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/election/delete/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).delete(anyLong());
    }

    // ---------------------------------------------------------
    // POST /election/changeStatus/{id}/{enabled}
    // ---------------------------------------------------------
    @Test
    void changeStatus_returns200WhenChanged() throws Exception {
        when(service.changeStatus(anyLong(), anyBoolean())).thenReturn(true);

        mockMvc.perform(post("/election/changeStatus/1/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).changeStatus(anyLong(), anyBoolean());
    }

    @Test
    void changeStatus_returns400WhenFails() throws Exception {
        when(service.changeStatus(anyLong(), anyBoolean())).thenReturn(null);

        mockMvc.perform(post("/election/changeStatus/1/false"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).changeStatus(anyLong(), anyBoolean());
    }
}
