package my.notinhas.project.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.login.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(String accessToken);
    IdTokenDTO createIdToken(String accessToken);
    LoginResponseDTO refresh();


}
