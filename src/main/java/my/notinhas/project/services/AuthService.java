package my.notinhas.project.services;

import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.login.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(String accessToken);
    IdTokenDTO createIdToken(String accessToken);
    LoginResponseDTO refresh();


}
