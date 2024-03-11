package my.notinhas.project.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class Requests {
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
        } catch (
                WebClientResponseException.BadRequest e) {
            // Lidar com erro 400 aqui, se necessário
            idTokenString = null; // Retornar um valor nulo ou outro valor padrão
        } catch (Exception e) {
            // Lidar com outros erros aqui, se necessário
            throw new RuntimeException("Erro ao fazer a chamada: " + e.getMessage());
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


}
