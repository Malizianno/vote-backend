package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.dto.NewsfeedPostFilterDTO;
import ro.cristiansterie.vote.service.NewsfeedService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class NewsfeedControllerTest extends GenericControllerTest {

    @MockBean
    private NewsfeedService service;

    // ---------------------------------------------------------
    // GET /newsfeed/{id}
    // ---------------------------------------------------------
    @Test
    void getById_returns200WhenPostExists() throws Exception {
        NewsfeedPostDTO dto = new NewsfeedPostDTO();
        dto.setId(1L);

        when(service.findById(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/newsfeed/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).findById(anyLong());
    }

    @Test
    void getById_returns400WhenPostIsNull() throws Exception {
        when(service.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/newsfeed/0"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).findById(anyLong());
    }

    // ---------------------------------------------------------
    // GET /newsfeed/all
    // ---------------------------------------------------------
    @Test
    void all_returns200WhenListExists() throws Exception {
        NewsfeedPostDTO p1 = new NewsfeedPostDTO();
        p1.setId(10L);

        NewsfeedPostDTO p2 = new NewsfeedPostDTO();
        p2.setId(20L);

        when(service.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/newsfeed/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[1].id").value(20L));

        verify(service, times(1)).findAll();
    }

    @Test
    void all_returns400WhenListIsNull() throws Exception {
        when(service.findAll()).thenReturn(null);

        mockMvc.perform(get("/newsfeed/all"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // POST /newsfeed/filtered
    // ---------------------------------------------------------
    @Test
    void filtered_returns200Always() throws Exception {
        NewsfeedPostFilterDTO filter = new NewsfeedPostFilterDTO();

        NewsfeedPostDTO dto = new NewsfeedPostDTO();
        dto.setId(5L);

        when(service.findFiltered(any())).thenReturn(List.of(dto));
        when(service.countFiltered(any())).thenReturn(1L);

        mockMvc.perform(
                post("/newsfeed/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].id").value(5L))
                .andExpect(jsonPath("$.total").value(1L));

        verify(service, times(1)).findFiltered(any());
        verify(service, times(1)).countFiltered(any());
    }

    // ---------------------------------------------------------
    // POST /newsfeed/add
    // ---------------------------------------------------------
    @Test
    void add_returns200WhenSaved() throws Exception {
        NewsfeedPostDTO toSave = new NewsfeedPostDTO();
        toSave.setId(0L);

        NewsfeedPostDTO saved = new NewsfeedPostDTO();
        saved.setId(100L);

        when(service.create(any())).thenReturn(saved);

        mockMvc.perform(
                post("/newsfeed/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L));

        verify(service, times(1)).create(any());
    }

    @Test
    void add_returns400WhenSaveFails() throws Exception {
        NewsfeedPostDTO toSave = new NewsfeedPostDTO();

        when(service.create(any())).thenReturn(null);

        mockMvc.perform(
                post("/newsfeed/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).create(any());
    }

    // ---------------------------------------------------------
    // PUT /newsfeed/update/{id}
    // ---------------------------------------------------------
    @Test
    void update_returns200WhenUpdated() throws Exception {
        NewsfeedPostDTO toUpdate = new NewsfeedPostDTO();
        toUpdate.setId(0L);

        NewsfeedPostDTO updated = new NewsfeedPostDTO();
        updated.setId(55L);

        when(service.update(anyLong(), any())).thenReturn(updated);

        mockMvc.perform(
                put("/newsfeed/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(55L));

        verify(service, times(1)).update(anyLong(), any());
    }

    @Test
    void update_returns400WhenUpdateFails() throws Exception {
        NewsfeedPostDTO toUpdate = new NewsfeedPostDTO();

        when(service.update(anyLong(), any())).thenReturn(null);

        mockMvc.perform(
                put("/newsfeed/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toUpdate)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).update(anyLong(), any());
    }

    // ---------------------------------------------------------
    // DELETE /newsfeed/delete/{id}
    // ---------------------------------------------------------
    @Test
    void delete_returns200Always() throws Exception {
        when(service.delete(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/newsfeed/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).delete(anyLong());
    }
}
