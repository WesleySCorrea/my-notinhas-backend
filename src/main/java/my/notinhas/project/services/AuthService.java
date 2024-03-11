package my.notinhas.project.services;

import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.LoginDTO;

public interface AuthService {
    LoginDTO login(String accessToken);
    IdTokenDTO createIdToken(String accessToken);
    UserDTO register(String accessToken);
}
