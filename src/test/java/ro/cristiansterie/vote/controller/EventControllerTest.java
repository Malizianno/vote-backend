package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.any;
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

import ro.cristiansterie.vote.dto.EventDTO;
import ro.cristiansterie.vote.dto.EventFilterDTO;
import ro.cristiansterie.vote.service.EventService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class EventControllerTest extends GenericControllerTest {

    @MockBean
    private EventService service;

    // ---------------------------------------------------------
    // GET /events/{id}
    // ---------------------------------------------------------
    @Test
    void one_returns200WhenEventExists() throws Exception {
        EventDTO dto = new EventDTO();
        dto.setId(1L);

        when(service.get(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).get(anyLong());
    }

    @Test
    void one_returns400WhenEventIsNull() throws Exception {
        when(service.get(anyLong())).thenReturn(null);

        mockMvc.perform(get("/events/0"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).get(anyLong());
    }

    // ---------------------------------------------------------
    // GET /events/all
    // ---------------------------------------------------------
    @Test
    void all_returns200WhenListExists() throws Exception {
        EventDTO e1 = new EventDTO();
        e1.setId(10L);

        EventDTO e2 = new EventDTO();
        e2.setId(20L);

        when(service.getAll()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/events/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[1].id").value(20L));

        verify(service, times(1)).getAll();
    }

    @Test
    void all_returns400WhenListIsNull() throws Exception {
        when(service.getAll()).thenReturn(null);

        mockMvc.perform(get("/events/all"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getAll();
    }

    // ---------------------------------------------------------
    // GET /events/last10
    // ---------------------------------------------------------
    @Test
    void last10_returns200WhenListExists() throws Exception {
        EventDTO e1 = new EventDTO();
        e1.setId(1L);

        EventDTO e2 = new EventDTO();
        e2.setId(2L);

        when(service.getLast10()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/events/last10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(service, times(1)).getLast10();
    }

    @Test
    void last10_returns400WhenListIsNull() throws Exception {
        when(service.getLast10()).thenReturn(null);

        mockMvc.perform(get("/events/last10"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getLast10();
    }

    // ---------------------------------------------------------
    // POST /events/filtered
    // ---------------------------------------------------------
    @Test
    void filtered_returns200WhenValid() throws Exception {
        EventFilterDTO filter = new EventFilterDTO();

        EventDTO dto = new EventDTO();
        dto.setId(99L);

        when(service.getFiltered(any())).thenReturn(List.of(dto));
        when(service.countFiltered(any())).thenReturn(1L);

        mockMvc.perform(
                post("/events/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].id").value(99L))
                .andExpect(jsonPath("$.total").value(1L));

        verify(service, times(1)).getFiltered(any());
        verify(service, times(1)).countFiltered(any());
    }

    @Test
    void filtered_returns400WhenListIsNull() throws Exception {
        EventFilterDTO filter = new EventFilterDTO();

        when(service.getFiltered(any())).thenReturn(null);
        when(service.countFiltered(any())).thenReturn(0L);

        mockMvc.perform(
                post("/events/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getFiltered(any());
        verify(service, times(1)).countFiltered(any());
    }

    // ---------------------------------------------------------
    // POST /events/save
    // ---------------------------------------------------------
    @Test
    void save_returns200WhenSaved() throws Exception {
        EventDTO toSave = new EventDTO();
        toSave.setId(0L);

        EventDTO saved = new EventDTO();
        saved.setId(123L);

        when(service.save(any())).thenReturn(saved);

        mockMvc.perform(
                post("/events/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123L));

        verify(service, times(1)).save(any());
    }

    @Test
    void save_returns400WhenSaveFails() throws Exception {
        EventDTO toSave = new EventDTO();

        when(service.save(any())).thenReturn(null);

        mockMvc.perform(
                post("/events/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).save(any());
    }

    // ---------------------------------------------------------
    // DELETE /events/delete/{id}
    // ---------------------------------------------------------
    @Test
    void delete_returns200WhenDeleted() throws Exception {
        when(service.delete(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/events/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).delete(anyLong());
    }

    @Test
    void delete_returns400WhenDeleteFails() throws Exception {
        when(service.delete(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/events/delete/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).delete(anyLong());
    }
}