package my.notinhas.project.services;

import my.notinhas.project.entities.AuthenticationToken;

import java.util.Optional;

public interface AuthenticationTokenService {

    Optional<AuthenticationToken> getRefreshToken(String token);
    void saveRefreshToken(AuthenticationToken authenticationToken);
    void deleteRefreshToken(String token);
}
