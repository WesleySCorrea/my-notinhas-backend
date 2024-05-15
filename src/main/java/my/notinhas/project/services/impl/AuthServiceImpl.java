package my.notinhas.project.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import my.notinhas.project.component.Auth;
import my.notinhas.project.component.GoogleHttpRequests;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.login.LoginResponseDTO;
import my.notinhas.project.entities.AuthenticationToken;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.services.AuthService;
import my.notinhas.project.services.AuthenticationTokenService;
import my.notinhas.project.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Auth auth;
    private final UserService service;
    private final GoogleHttpRequests requests;
    private final AuthenticationTokenService authenticationTokenService;
    @Override
    public LoginResponseDTO login(String code) {

        IdTokenDTO idTokenDTO = this.createIdToken(code);

        GoogleIdToken googleIdToken = auth.extractAndVerifyIdToken(idTokenDTO.getId_token());

        String userEmail = googleIdToken.getPayload().getEmail();

        UserDTO userDTO;
        if (service.existsByEmail(userEmail)) {
            userDTO = service.findByEmail(userEmail);

            if (userDTO.getActive().equals(false)) {
                userDTO.setActive(true);
                this.activeUser(userDTO);
            }
        } else {
            userDTO = this.register(googleIdToken);
        }
        var token = idTokenDTO.getId_token();
        var refreshToken = idTokenDTO.getRefresh_token();
        var cacheToken = this.shroten(token);
        this.authenticationTokenService
                .saveRefreshToken(
                        new AuthenticationToken(
                                cacheToken,
                                refreshToken,
                                userDTO.getUserName(),
                                userDTO.getPicture(),
                                LocalDateTime.now().plusMinutes(65L)));
        return new LoginResponseDTO(idTokenDTO.getId_token(), userDTO.getUserName(), userDTO.getPicture());
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
    public UserDTO register(GoogleIdToken googleIdToken) {

        UserDTO userDTO = new UserDTO().createUserDTOWithUserName(googleIdToken);
        UserDTO newUserDTO;

        try {
            newUserDTO = service.saveUsers(userDTO);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }

        return newUserDTO;
    }

    @Override
    public UserDTO activeUser(UserDTO userDTO) {

        UserDTO newUserDTO;
        try {
            newUserDTO = service.saveUsers(userDTO);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }

        return newUserDTO;
    }

    @Override
    public LoginResponseDTO refresh() {
        var token = this.extractToken();
        var cacheToken = this.shroten(token);
        var authenticationToken = this.authenticationTokenService.getRefreshToken(cacheToken);
        if (authenticationToken.isPresent()) {
            var refreshToken = authenticationToken.get().getRefreshToken();
            var picture = authenticationToken.get().getPicture();
            var userName = authenticationToken.get().getUserName();
            this.authenticationTokenService.deleteRefreshToken(cacheToken);
            var idTokenDTO = this.requests.refreshTokenRequest(refreshToken);
            var newCacheToken = this.shroten(idTokenDTO.getId_token());
            this.authenticationTokenService
                    .saveRefreshToken(
                            new AuthenticationToken(
                                    newCacheToken,
                                    refreshToken,
                                    userName,
                                    picture,
                                    LocalDateTime.now().plusMinutes(65L)));
            return new LoginResponseDTO(idTokenDTO.getId_token(), userName, picture);
        }
        throw new ObjectNotFoundException("Invalid token");
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