package my.notinhas.project.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.exception.runtime.CallHttpErrorException;
import my.notinhas.project.exception.runtime.ObjectConversionException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
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

    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;


    public IdTokenDTO idTokenRequest(String code) {

        WebClient webClient = WebClient.create("https://oauth2.googleapis.com");

        String idTokenString;
        try {
            idTokenString = webClient.post()
                    .uri("/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(
                            "client_id", clientId)
                            .with("code", code)
                            .with("client_secret", clientSecret)
                            .with("redirect_uri", "postmessage")
                            .with("access_type", "offline")
                            .with("grant_type", "authorization_code"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new CallHttpErrorException("Error making the HTTP call: " + e.getMessage());
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
    public Boolean validedTokenId (IdTokenDTO idTokenDTO) {

        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            verifier.verify(idTokenDTO.getId_token());
        } catch (GeneralSecurityException | IOException e) {
            throw new UnauthorizedIdTokenException("Invalid or expired token id");
        }
        return true;
    }
}
