package my.notinhas.project.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import my.notinhas.project.component.Auth;
import my.notinhas.project.component.HttpRequests;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.login.LoginResponseDTO;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.services.AuthService;
import my.notinhas.project.services.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Auth auth;
    private final UserService service;
    private final HttpRequests requests;

    @Override
    public LoginResponseDTO login(String accessToken) {

        IdTokenDTO idTokenDTO = this.createIdToken(accessToken);

        GoogleIdToken googleIdToken = auth.extractAndVerifyIdToken(idTokenDTO.getId_token());

        String userEmail = googleIdToken.getPayload().getEmail();

        UserDTO userDTO;
        if (service.existsByEmail(userEmail)) {
            userDTO = service.findByEmail(userEmail);
        } else {
            userDTO = this.register(googleIdToken);
        }

        return new LoginResponseDTO(idTokenDTO, userDTO);
    }

    @Override
    public IdTokenDTO createIdToken(String access_token) {

        IdTokenDTO idTokenDTO = requests.idTokenRequest(access_token);
        if (idTokenDTO == null) {
            throw new UnauthorizedIdTokenException("Invalid or expired access token");
        }

        requests.validedTokenId(idTokenDTO);

        return idTokenDTO;
    }

    @Override
    public UserDTO register(GoogleIdToken googleIdToken) {

        UserDTO userDTO = new UserDTO().createUserDTOWithUserName(googleIdToken);

        UserDTO newUserDTO = service.saveUsers(userDTO);

        return newUserDTO;
    }

}