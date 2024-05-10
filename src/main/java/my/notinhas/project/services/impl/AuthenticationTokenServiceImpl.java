package my.notinhas.project.services.impl;

import lombok.RequiredArgsConstructor;
import my.notinhas.project.entities.AuthenticationToken;
import my.notinhas.project.repositories.AuthenticationTokenRepository;
import my.notinhas.project.services.AuthenticationTokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {

    private final AuthenticationTokenRepository authenticationTokenRepository;

    @Override
    public Optional<AuthenticationToken> getRefreshToken(String token) {
        return this.authenticationTokenRepository.findById(token);
    }

    @Override
    public void saveRefreshToken(AuthenticationToken authenticationToken) {
        this.authenticationTokenRepository.save(authenticationToken);
    }

    @Override
    public void deleteRefreshToken(String token) {
        this.authenticationTokenRepository.deleteById(token);
    }
}
