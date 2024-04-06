package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.response.UserIDResponseDTO;
import my.notinhas.project.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {

        List<UserDTO> users = this.service.findAll();

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserIDResponseDTO> findById(@PathVariable Long id) {

        UserIDResponseDTO user = this.service.findByID(id);

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
}
