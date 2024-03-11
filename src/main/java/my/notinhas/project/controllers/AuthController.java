package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.auth.LoginDTO;
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

    @PostMapping("/login/{accessToken}")
    public ResponseEntity<LoginDTO> login(@PathVariable String accessToken) {

        LoginDTO loginDTO = service.login(accessToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginDTO);
    }

    @PostMapping("/idtoken/{accessToken}")
    public ResponseEntity<IdTokenDTO> createIdToken(@PathVariable String accessToken) {

        IdTokenDTO idTokenDTO = service.createIdToken(accessToken);

        if (idTokenDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(idTokenDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register/{accessToken}")
    public ResponseEntity<UserDTO> registerUser(@PathVariable String accessToken) {

        UserDTO userDTO = service.register(accessToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
}
