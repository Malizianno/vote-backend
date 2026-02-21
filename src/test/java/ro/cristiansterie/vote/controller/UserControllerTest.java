package ro.cristiansterie.vote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
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

import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.service.UserService;
import ro.cristiansterie.vote.util.GenericControllerTest;
import ro.cristiansterie.vote.util.UserRoleEnum;

class UserControllerTest extends GenericControllerTest {

    @MockBean
    private UserService service;

    // ---------------------------------------------------------
    // GET /users/{id}
    // ---------------------------------------------------------
    @Test
    void one_returns200WhenUserExists() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setId(1L);

        when(service.get(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).get(anyLong());
    }

    @Test
    void one_returns400WhenUserIsNull() throws Exception {
        when(service.get(anyLong())).thenReturn(null);

        mockMvc.perform(get("/users/0"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).get(anyLong());
    }

    // ---------------------------------------------------------
    // GET /users/all
    // ---------------------------------------------------------
    @Test
    void all_returns200WhenListExists() throws Exception {
        UserDTO u1 = new UserDTO();
        u1.setId(10L);

        UserDTO u2 = new UserDTO();
        u2.setId(20L);

        when(service.getAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[1].id").value(20L));

        verify(service, times(1)).getAll();
    }

    @Test
    void all_returns400WhenListIsNull() throws Exception {
        when(service.getAll()).thenReturn(null);

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getAll();
    }

    // ---------------------------------------------------------
    // POST /users/filtered
    // ---------------------------------------------------------
    @Test
    void filtered_returns200WhenUsersExist() throws Exception {
        UserFilterDTO filter = new UserFilterDTO();

        UserDTO dto = new UserDTO();
        dto.setId(5L);
        filter.setUser(dto);

        when(service.getFiltered(any())).thenReturn(List.of(dto));
        when(service.countFiltered(any())).thenReturn(10L);

        // admin count
        when(service.countFiltered(filter)).thenReturn(2L);

        // votant count
        when(service.countFiltered(argThat(f -> {
            return f.getObject() != null && f.getObject().getRole() == UserRoleEnum.VOTANT;
        }))).thenReturn(8L);

        mockMvc.perform(
                post("/users/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value(5L))
                .andExpect(jsonPath("$.total").value(10L))
                .andExpect(jsonPath("$.adminUsersCount").value(10L))
                .andExpect(jsonPath("$.votantUsersCount").value(8L));

        verify(service, times(1)).getFiltered(any());
        verify(service, atLeast(3)).countFiltered(any());
    }

    @Test
    void filtered_returns400WhenUsersListIsNull() throws Exception {
        UserFilterDTO filter = new UserFilterDTO();

        when(service.getFiltered(any())).thenReturn(null);
        when(service.countFiltered(any())).thenReturn(0L);

        mockMvc.perform(
                post("/users/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getFiltered(any());
        verify(service, atLeast(3)).countFiltered(any());
    }

    // ---------------------------------------------------------
    // POST /users/save
    // ---------------------------------------------------------
    @Test
    void save_returns200WhenSaved() throws Exception {
        UserDTO toSave = new UserDTO();
        toSave.setId(0L);

        UserDTO saved = new UserDTO();
        saved.setId(100L);

        when(service.save(any())).thenReturn(saved);

        mockMvc.perform(
                post("/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L));

        verify(service, times(1)).save(any());
    }

    @Test
    void save_returns400WhenSaveFails() throws Exception {
        UserDTO toSave = new UserDTO();

        when(service.save(any())).thenReturn(null);

        mockMvc.perform(
                post("/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).save(any());
    }

    // ---------------------------------------------------------
    // DELETE /users/delete/{id}
    // ---------------------------------------------------------
    @Test
    void delete_returns200WhenDeleted() throws Exception {
        when(service.delete(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(service, times(1)).delete(anyLong());
    }

    @Test
    void delete_returns400WhenDeleteFails() throws Exception {
        when(service.delete(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).delete(anyLong());
    }

    // ---------------------------------------------------------
    // GET /users/profile/{id}
    // ---------------------------------------------------------
    @Test
    void profile_returns200WhenProfileExists() throws Exception {
        UserVoterDTO profile = new UserVoterDTO();
        profile.setId(7L);

        when(service.getProfile(anyLong())).thenReturn(profile);

        mockMvc.perform(get("/users/profile/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L));

        verify(service, times(1)).getProfile(anyLong());
    }

    @Test
    void profile_returns400WhenProfileIsNull() throws Exception {
        when(service.getProfile(anyLong())).thenReturn(null);

        mockMvc.perform(get("/users/profile/7"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).getProfile(anyLong());
    }

    // ---------------------------------------------------------
    // POST /users/profile/save
    // ---------------------------------------------------------
    @Test
    void saveProfile_returns200WhenSaved() throws Exception {
        UserVoterDTO request = new UserVoterDTO();

        UserVoterDTO saved = new UserVoterDTO();
        saved.setId(33L);

        when(service.saveProfile(any())).thenReturn(saved);

        mockMvc.perform(
                post("/users/profile/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(33L));

        verify(service, times(1)).saveProfile(any());
    }

    @Test
    void saveProfile_returns400WhenSaveFails() throws Exception {
        UserVoterDTO request = new UserVoterDTO();

        when(service.saveProfile(any())).thenReturn(null);

        mockMvc.perform(
                post("/users/profile/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).saveProfile(any());
    }
}
