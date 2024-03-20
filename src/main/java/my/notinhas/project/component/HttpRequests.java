package my.notinhas.project.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class HttpRequests {
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;


    public IdTokenDTO idTokenRequest(String access_token) {

        WebClient webClient = WebClient.create("https://oauth2.googleapis.com");

        String idTokenString;
        try {
            idTokenString = webClient.post()
                    .uri("/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("refresh_token", access_token)
                            .with("grant_type", "refresh_token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
//            throw new CallHttpErrorException("Error making the HTTP call: " + e.getMessage());
            throw new RuntimeException("Error making the HTTP call: " + e.getMessage());
        }

        if (idTokenString != null) {
            try {
                return objectMapper.readValue(idTokenString, IdTokenDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    public Boolean validedTokenId (IdTokenDTO idTokenDTO) {

        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            verifier.verify(idTokenDTO.getId_token());
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return true;
    }

//    public GetInfo getInfo(String accessToken) {
//        WebClient webClient = WebClient.create("https://www.googleapis.com");
//
//        String getInfoString;
//        try {
//            getInfoString = webClient.get()
//                    .uri("/oauth2/v2/userinfo")
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//        } catch (WebClientResponseException.Unauthorized e) {
//            throw new UnauthorizedAccessTokenException("Access token is unauthorized or invalid");
//        } catch (Exception e) {
//            throw new CallHttpErrorException("Error making the HTTP call: " + e.getMessage());
//        }
//
//        if (getInfoString != null) {
//            try {
//                return objectMapper.readValue(getInfoString, GetInfo.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return null;
//    }

//    public IdTokenInfoDTO getIdToken(String idToken) {
//
//        WebClient webClient = WebClient.create("https://www.googleapis.com");
//
//        try {
//            IdTokenInfoDTO idTokenInfo = webClient.get()
//                    .uri("/oauth2/v1/tokeninfo?id_token=" + idToken)
//                    .retrieve()
//                    .bodyToMono(IdTokenInfoDTO.class)
//                    .block();
//
//            return idTokenInfo;
//        } catch (WebClientResponseException e) {
//            throw new UnauthorizedAccessTokenException("Access token is unauthorized or invalid");
//        } catch (Exception e) {
//            throw new CallHttpErrorException("Error making the HTTP call: " + e.getMessage());
//        }
//    }
}
