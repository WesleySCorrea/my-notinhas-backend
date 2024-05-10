package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.auth.login.LoginRequestDTO;
import my.notinhas.project.dtos.auth.login.LoginResponseDTO;
import my.notinhas.project.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        LoginResponseDTO loginResponseDTO = service.login(loginRequestDTO.getCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDTO);
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> login() {

        LoginResponseDTO loginResponseDTO = service.refresh();

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDTO);
    }
}
