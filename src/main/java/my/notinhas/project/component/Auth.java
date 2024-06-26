package my.notinhas.project.component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class Auth {
    private final Variables variables;

    public GoogleIdToken extractAndVerifyIdToken(String idToken) {

        GoogleIdTokenVerifier verifier;
        try {
            verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(variables.getClientId()))
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new UnauthorizedIdTokenException("Invalid or expired token id");
        }

        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idToken);
            return googleIdToken;
        } catch (GeneralSecurityException | IOException e) {
            throw new UnauthorizedIdTokenException("Token invalid or expired");
        }
    }
}
