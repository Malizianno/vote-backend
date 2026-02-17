package ro.cristiansterie.vote.faceid.service;

import java.lang.invoke.MethodHandles;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ro.cristiansterie.vote.faceid.dto.FaceVerificationRequest;
import ro.cristiansterie.vote.faceid.dto.FaceVerificationResponse;
import ro.cristiansterie.vote.faceid.dto.FaceVerificationResult;
import ro.cristiansterie.vote.service.LoginService;
import ro.cristiansterie.vote.service.UserService;

@Service
public class FaceVerificationService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;
    private final LoginService loginService;

    private final RestTemplate restTemplate = new RestTemplate();

    public FaceVerificationService(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    public FaceVerificationResponse loginWithFace(FaceVerificationRequest request) {
        Map<Long, byte[]> referenceBase64ListBytes = userService.getAllFaceImagesBase64();
        Map<Long, String> referenceBase64List = referenceBase64ListBytes.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> Base64.getEncoder().encodeToString(entry.getValue())));

        FaceVerificationResult result = verifyFace(request.getImageBase64(),
                referenceBase64List.values().stream().toList());

        if (null == result) {
            log.error("Error while verifying face with Deepface algorithm...");
            return null;
        }

        if (result.isMatch()) {
            log.info("FaceID: Match found with distance: " + result.getDistance());
            log.info("FaceID: Result: {}", result);

            var response = new FaceVerificationResponse();
            String matchedBase64 = result.getReferenceBase64();

            Optional<Long> matchedId = referenceBase64List.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(matchedBase64))
                    .map(Map.Entry::getKey)
                    .findFirst();

            if (matchedId.isPresent()) {
                var user = userService.getVoter(matchedId.get());

                response.setHasVoted(user.getHasVoted());
                response.setId(user.getId());
                response.setRole(user.getRole());
                response.setToken(loginService.loginUserWithFace(user));

                log.info("FaceID: User VOTANT Authenticated with face");

                return response;
            } else {
                log.error("FaceID: No ID from DB matched!");
            }
        } else {
            log.error("FaceID: No matched ID found.");
        }

        return null;
    }

    public FaceVerificationResult verifyFace(String imageBase64, List<String> referenceBase64List) {
        String url = "http://localhost:5001/verify";

        Map<String, Object> payload = new HashMap<>();
        payload.put("imageBase64", imageBase64);
        payload.put("referenceBase64List", referenceBase64List);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<FaceVerificationResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    FaceVerificationResult.class);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Face verification service failed: {}", e.getMessage());
        }

        return null;
    }
}