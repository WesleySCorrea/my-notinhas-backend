package my.notinhas.project.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import my.notinhas.project.component.Auth;
import my.notinhas.project.component.GoogleHttpRequests;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.login.LoginResponseDTO;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.services.AuthService;
import my.notinhas.project.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Auth auth;
    private final UserService service;
    private final GoogleHttpRequests requests;
    @Override
    public LoginResponseDTO login(String code) {

        IdTokenDTO idTokenDTO = this.createIdToken(code);

        GoogleIdToken googleIdToken = auth.extractAndVerifyIdToken(idTokenDTO.getIdToken());

        String userEmail = googleIdToken.getPayload().getEmail();

        var users = service.findByEmail(userEmail);

        if (users != null) {
            if (!users.getActive()) {
                this.service.activeUser(userEmail);
            }
        } else {
            users = this.service.saveUsers(googleIdToken);
        }
        var shortToken = this.shroten(idTokenDTO.getIdToken());
        var refreshToken = idTokenDTO.getRefreshToken();
        this.service.saveTokenAndRefreshToken(users.getId(), refreshToken, shortToken);
        return new LoginResponseDTO(idTokenDTO.getIdToken(), users.getUserName(), users.getPicture());
    }

    @Override
    public IdTokenDTO createIdToken(String code) {

        IdTokenDTO idTokenDTO = requests.idTokenRequest(code);
        if (idTokenDTO == null) {
            throw new UnauthorizedIdTokenException("Invalid or expired access token");
        }

        requests.validedTokenId(idTokenDTO);

        return idTokenDTO;
    }

    @Override
    public LoginResponseDTO refresh() {
        try {
            var token = this.extractToken();
            var shortToken = this.shroten(token);
            var user = service.findUserByIdToken(shortToken);
            var idTokenDTO = this.requests.refreshTokenRequest(user.getRefreshToken());
            var newShortToken = this.shroten(idTokenDTO.getIdToken());
            this.service.updateToken(user.getId(), newShortToken);
            return new LoginResponseDTO(idTokenDTO.getIdToken(), user.getUserName(), user.getPicture());
        } catch (Exception e) {
            throw new ObjectNotFoundException("Invalid token");
        }
    }

    private String extractToken() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }

    private String shroten(String token) {
        var tokenLength = token.length();
        return token.substring(tokenLength - 50);
    }
}