package my.notinhas.project.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import my.notinhas.project.component.Auth;
import my.notinhas.project.component.GoogleHttpRequests;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.exception.FieldMessage;
import my.notinhas.project.exception.ValidationError;
import my.notinhas.project.exception.runtime.CallHttpErrorException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.services.AuthenticationTokenService;
import my.notinhas.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final GoogleHttpRequests googleHttpRequests;
    private final AuthenticationTokenService authenticationTokenService;


    @Autowired
    private Auth auth;

    @Value("${google.client-id}")
    private String clientId;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (isAuthenticationNotRequired(request)) {
            chain.doFilter(request, response);
            return;
        }

        String idToken = extractTokenFromHeader(request);

        if (idToken == null) {
            throw new UnauthorizedIdTokenException("Without Token");
        }
        GoogleIdToken googleIdToken = this.extractAndVerifyIdToken(idToken);


        try {
        if (googleIdToken != null) {

            UserDTO userDTO = userService.findByEmail(googleIdToken.getPayload().getEmail());

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(userDTO, null, null));

        } else {
            throw new UnauthorizedIdTokenException("Token invalid or expired");
        }
        chain.doFilter(request, response);

        } catch (UnauthorizedIdTokenException e) {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            ValidationError err = new ValidationError();
            err.setStatus(status.value());
            err.setTimestamp(Instant.now());
            err.setError(e.getMessage());
            err.setPath(request.getRequestURI());
            err.setErrors(Collections.singletonList(new FieldMessage("UnauthorizedIdTokenException", e.getMessage())));
            response.setStatus(status.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            response.getWriter().write(mapper.writeValueAsString(err));
            return;
        }
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
            throw new CallHttpErrorException("Error making the HTTP call: " + e.getMessage());
        }

        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idToken);
            return googleIdToken;
        } catch (GeneralSecurityException | IOException e) {
            throw new UnauthorizedIdTokenException("Token invalid or expired");
        }
    }

    private boolean isAuthenticationNotRequired(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> permitAllPatterns = Arrays.asList("/post/public", "/auth/**");

        return permitAllPatterns.stream().anyMatch(pattern -> matcher.match(pattern, request.getServletPath()));
    }
}