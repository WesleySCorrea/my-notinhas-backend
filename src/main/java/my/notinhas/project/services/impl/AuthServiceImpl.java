package my.notinhas.project.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.AllArgsConstructor;
import my.notinhas.project.component.Requests;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.GetInfo;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.LoginDTO;
import my.notinhas.project.entities.Users;
import my.notinhas.project.repositories.UserRepository;
import my.notinhas.project.services.AuthService;
import my.notinhas.project.services.UserService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Requests requests;
    private final ModelMapper mapper;
    private final UserService service;

    @Override
    public LoginDTO login(String accessToken) {

        IdTokenDTO idTokenDTO = this.createIdToken(accessToken);

        GetInfo getInfo = requests.getInfo(idTokenDTO.access_token);

        UserDTO userDTO;
        if (service.existsByEmail(getInfo.getEmail())) {
            userDTO = service.findByEmail(getInfo.getEmail());
        } else {
            userDTO = this.register(getInfo);
        }

        return new LoginDTO(idTokenDTO, userDTO);
    }

    @Override
    public IdTokenDTO createIdToken(String access_token) {

        IdTokenDTO idTokenDTO = requests.idTokenRequest(access_token);
        if (idTokenDTO == null) {
            System.out.println("Invalid access_token");
            return null;
        }

        requests.validedTokenId(idTokenDTO);

        return idTokenDTO;
    }

    @Override
    public UserDTO register(GetInfo getInfo) {

        UserDTO userDTO = createUserDTOWithUserName(getInfo);

        UserDTO newUserDTO = service.saveUsers(userDTO);

        return newUserDTO;
    }

    private UserDTO createUserDTOWithUserName (GetInfo getInfo) {

        char number1 = getInfo.getGoogleId().charAt(3);
        char number2 = getInfo.getGoogleId().charAt(9);
        char number3 = getInfo.getGoogleId().charAt(11);
        char number4 = getInfo.getGoogleId().charAt(15);
        char number5 = getInfo.getGoogleId().charAt(20);

        String userName = getInfo.getFirstName() + number1 + number2 + number3 + number4 + number5;

        UserDTO userDTO = mapper.map(getInfo, UserDTO.class);
        userDTO.setUserName(userName);

        return userDTO;
    }
}