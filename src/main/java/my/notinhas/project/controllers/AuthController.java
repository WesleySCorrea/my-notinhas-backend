package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.LoginDTO;
import my.notinhas.project.dtos.auth.LoginRequestDTO;
import my.notinhas.project.services.AuthService;
import my.notinhas.project.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        LoginDTO loginDTO = service.login(loginRequestDTO.getAccessToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(loginDTO);
    }

    @PostMapping("/idtoken")
    public ResponseEntity<IdTokenDTO> createIdToken(@RequestBody LoginRequestDTO loginRequestDTO) {

        IdTokenDTO idTokenDTO = service.createIdToken(loginRequestDTO.getAccessToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(idTokenDTO);
    }
}
