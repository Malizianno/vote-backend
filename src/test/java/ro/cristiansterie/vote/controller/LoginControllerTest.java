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

import ro.cristiansterie.vote.dto.LoginRequestDTO;
import ro.cristiansterie.vote.dto.LoginResponseDTO;
import ro.cristiansterie.vote.dto.LogoutRequestDTO;
import ro.cristiansterie.vote.dto.LogoutResponseDTO;
import ro.cristiansterie.vote.faceid.dto.FaceVerificationRequest;
import ro.cristiansterie.vote.faceid.dto.FaceVerificationResponse;
import ro.cristiansterie.vote.faceid.service.FaceVerificationService;
import ro.cristiansterie.vote.service.LoginService;
import ro.cristiansterie.vote.util.GenericControllerTest;

class LoginControllerTest extends GenericControllerTest {

    @MockBean
    private LoginService service;

    @MockBean
    private FaceVerificationService faceService;

    // ---------------------------------------------------------
    // POST /login/
    // ---------------------------------------------------------
    @Test
    void login_returns200WhenResponseExists() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("abc123");

        when(service.login(any())).thenReturn(response);

        mockMvc.perform(
                post("/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("abc123"));

        verify(service, times(1)).login(any());
    }

    @Test
    void login_returns400WhenResponseIsNull() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();

        when(service.login(any())).thenReturn(null);

        mockMvc.perform(
                post("/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).login(any());
    }

    // ---------------------------------------------------------
    // POST /login/logout
    // ---------------------------------------------------------
    @Test
    void logout_returns200WhenResponseExists() throws Exception {
        LogoutRequestDTO request = new LogoutRequestDTO();
        LogoutResponseDTO response = new LogoutResponseDTO();
        response.setResponse(true);

        when(service.logout(any())).thenReturn(response);

        mockMvc.perform(
                post("/login/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(true));

        verify(service, times(1)).logout(any());
    }

    @Test
    void logout_returns400WhenResponseIsNull() throws Exception {
        LogoutRequestDTO request = new LogoutRequestDTO();

        when(service.logout(any())).thenReturn(null);

        mockMvc.perform(
                post("/login/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).logout(any());
    }

    // ---------------------------------------------------------
    // POST /login/face
    // ---------------------------------------------------------
    @Test
    void verify_returns200WhenResultExists() throws Exception {
        FaceVerificationRequest request = new FaceVerificationRequest();
        FaceVerificationResponse result = new FaceVerificationResponse();
        result.setId(1L);

        when(faceService.loginWithFace(any())).thenReturn(result);

        mockMvc.perform(
                post("/login/face")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(faceService, times(1)).loginWithFace(any());
    }

    @Test
    void verify_returns400WhenResultIsNull() throws Exception {
        FaceVerificationRequest request = new FaceVerificationRequest();

        when(faceService.loginWithFace(any())).thenReturn(null);

        mockMvc.perform(
                post("/login/face")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(faceService, times(1)).loginWithFace(any());
    }
}
