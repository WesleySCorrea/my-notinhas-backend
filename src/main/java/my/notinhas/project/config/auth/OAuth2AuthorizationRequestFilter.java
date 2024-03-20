package my.notinhas.project.config.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class OAuth2AuthorizationRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    @Value("${google.client-id}")
    private String clientId;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (isAuthenticationNotRequired(request)) {
            chain.doFilter(request, response); // Não executa o filtro de autenticação e passa para o próximo filtro
            return;
        }

        String idToken = extractTokenFromHeader(request);

        GoogleIdToken googleIdToken = this.extractAndVerifyIdToken(idToken);

        if (googleIdToken != null) {
            UserDTO userDTO = userService.findByEmail(googleIdToken.getPayload().getEmail());

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(userDTO, null, null));
        }

        chain.doFilter(request, response);
    }

    public static String extractTokenFromHeader(HttpServletRequest request) {
        String idToken = request.getHeader("Authorization");
        if (idToken != null && idToken.startsWith("Bearer ")) {
            return idToken.replace("Bearer ", "").trim();
        }
        return null;
    }

    private GoogleIdToken extractAndVerifyIdToken(String idToken) {

        GoogleIdTokenVerifier verifier;
        try {
            verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idToken);
            return googleIdToken;
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAuthenticationNotRequired(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> permitAllPatterns = Arrays.asList("/post/public", "/auth/**"); // Adicione mais endpoints permitAll, se necessário

        return permitAllPatterns.stream().anyMatch(pattern -> matcher.match(pattern, request.getServletPath()));
    }
}