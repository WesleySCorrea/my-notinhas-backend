package my.notinhas.project.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.exception.runtime.ObjectConversionException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GoogleHttpRequests {

    private final Variables variables;


    public IdTokenDTO idTokenRequest(String code) {

        WebClient webClient = WebClient.create("https://oauth2.googleapis.com");

        String idTokenString;
        try {
            idTokenString = webClient.post()
                    .uri("/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(
                            "client_id", variables.getClientId())
                            .with("code", code)
                            .with("client_secret", variables.getClientSecret())
                            .with("redirect_uri", "postmessage")
                            .with("access_type", "offline")
                            .with("grant_type", "authorization_code"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new UnauthorizedIdTokenException("Error making the HTTP call: " + e.getMessage());
        }

        if (idTokenString != null) {
            try {
                return new ObjectMapper().readValue(idTokenString, IdTokenDTO.class);
            } catch (JsonProcessingException e) {
                throw new ObjectConversionException("error when trying to convert the object to IdToken");
            }
        }
        return null;
    }

    public IdTokenDTO refreshTokenRequest(String refreshToken) {
        WebClient webClient = WebClient.create("https://oauth2.googleapis.com");

        String refreshTokenResponse;
        try {
            refreshTokenResponse = webClient.post()
                    .uri("/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("client_id", variables.getClientId())
                            .with("client_secret", variables.getClientSecret())
                            .with("refresh_token", refreshToken)
                            .with("grant_type", "refresh_token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new UnauthorizedIdTokenException("Error making the HTTP call: " + e.getMessage());
        }

        if (refreshTokenResponse != null) {
            try {
                return new ObjectMapper().readValue(refreshTokenResponse, IdTokenDTO.class);
            } catch (JsonProcessingException e) {
                throw new ObjectConversionException("Error when trying to convert the object to IdToken");
            }
        }
        return null;
    }
    public Boolean validedTokenId (IdTokenDTO idTokenDTO) {

        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(variables.getClientId()))
                .build();

        try {
            verifier.verify(idTokenDTO.getIdToken());
        } catch (GeneralSecurityException | IOException e) {
            throw new UnauthorizedIdTokenException("Invalid or expired token id");
        }
        return true;
    }


}
