package ro.cristiansterie.vote.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import ro.cristiansterie.vote.config.CustomAuthenticationManager;
import ro.cristiansterie.vote.config.FaceIDAuthentication;
import ro.cristiansterie.vote.dto.LoginRequestDTO;
import ro.cristiansterie.vote.dto.LoginResponseDTO;
import ro.cristiansterie.vote.dto.LogoutRequestDTO;
import ro.cristiansterie.vote.dto.LogoutResponseDTO;
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.util.JWTUtils;
import ro.cristiansterie.vote.util.TestConstants;
import ro.cristiansterie.vote.util.UserRoleEnum;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private CustomAuthenticationManager authenticationManager;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginService service;

    @Test
    void login_returnsExpectedValue() {
        // Arrange
        UserDetails userDetails = User.withUsername("test").password("password").authorities("ADMIN").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword());
        String jwt = "mock-jwt";

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername(userDetails.getUsername());
        loginRequest.setPassword(userDetails.getPassword());
        loginRequest.setRole(UserRoleEnum.ADMIN);

        when(userService.loadUserByUsername("test")).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJWTToken(any(UsernamePasswordAuthenticationToken.class))).thenReturn(jwt);

        // Act
        LoginResponseDTO result = service.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(jwt, result.getToken());

        verify(userService).loadUserByUsername(anyString());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJWTToken(any(Authentication.class));
    }

    @Test
    void loginUserWithFace_returnsExpectedValue() {
        // Arrange
        String faceIdToken = "face-id-test";
        UserVoterDTO voter = new UserVoterDTO();
        voter.setId(1L);
        voter.setRole(UserRoleEnum.VOTANT);
        voter.setFaceImage(TestConstants.IMAGE_TEST);

        Authentication authentication = new FaceIDAuthentication("face-id-test");

        when(userService.getVoter(1L)).thenReturn(voter);
        when(authenticationManager.authenticate(any(FaceIDAuthentication.class))).thenReturn(authentication);
        when(jwtUtils.generateJWTToken(any(Authentication.class))).thenReturn(faceIdToken);

        // Act
        String result = service.loginUserWithFace(voter);

        // Assert
        assertNotNull(result);
        assertEquals(faceIdToken, result);
    }

    @Test
    @Transactional
    void logout_returnsExpectedValue() {
        // Arrange
        LogoutRequestDTO logoutRequest = new LogoutRequestDTO();
        logoutRequest.setUsername("test");

        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "password",
                List.of(() -> "ADMIN"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        LogoutResponseDTO result = service.logout(logoutRequest);

        // Assert
        assertNotNull(result);
        assertEquals(result.getUsername(), logoutRequest.getUsername());
        assertTrue(result.isResponse());
    }
}
