package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.response.CommentToUserDTO;
import my.notinhas.project.dtos.response.LikeToUserDTO;
import my.notinhas.project.dtos.response.UserHistoryResponseDTO;
import my.notinhas.project.dtos.response.UserIDResponseDTO;
import my.notinhas.project.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<UserDTO> findTokenUser() {

        var users = this.service.findByToken();

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<Page<UserHistoryResponseDTO>> userHistory(@PathVariable Long id, Pageable pageable) {

        Page<UserHistoryResponseDTO> userHistory = this.service.findHistoryByUserId(id, pageable);

        return ResponseEntity.ok().body(userHistory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserIDResponseDTO> findById(@PathVariable Long id) {

        UserIDResponseDTO user = this.service.findByID(id);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{id}/reactions")
    public ResponseEntity<Page<LikeToUserDTO>> findReactionsByUserId(@PathVariable Long id, Pageable pageable) {

        Page<LikeToUserDTO> pageLikes = this.service.findReactionsByUserId(id, pageable);

        return ResponseEntity.ok().body(pageLikes);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentToUserDTO>> findCommentsByUserId(@PathVariable Long id, Pageable pageable) {

        Page<CommentToUserDTO> pageLikes = this.service.findCommentsByUserId(id, pageable);

        return ResponseEntity.ok().body(pageLikes);
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
