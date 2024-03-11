package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final IUserService service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {

        List<UserDTO> users = this.service.findAll();

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {

        UserDTO user = this.service.findByID(id);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {

        UserDTO user = this.service.saveUsers(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO, @PathVariable Long id) {

        UserDTO userUpdated = this.service.updateUser(userDTO, id);

        return ResponseEntity.ok(userUpdated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {

        this.service.deleteByID(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getinfo/{accessToken}")
    public ResponseEntity<IdTokenDTO> getInfo(@PathVariable String accessToken) {

        IdTokenDTO idTokenDTO = service.validateIdToken(accessToken);

        if (idTokenDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(idTokenDTO); // Token válido, retorna 200
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Token inválido, retorna 400
        }
    }
}
