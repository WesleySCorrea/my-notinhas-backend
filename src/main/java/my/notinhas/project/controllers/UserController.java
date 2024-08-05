package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.UpdateUserRequestDTO;
import my.notinhas.project.dtos.response.*;
import my.notinhas.project.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/username/{userName}")
    public ResponseEntity<UserProfileDTO> findByUsername(@PathVariable String userName) {

        UserProfileDTO user = this.service.findByUserName(userName);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/username/with/{userName}")
    public ResponseEntity<Page<UserProfileDTO>> findByUsernameWith(@PathVariable String userName, Pageable pageable) {

        Page<UserProfileDTO> users = this.service.findByUserNameContaining(userName, pageable);

        if (users == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/username/exists/{userName}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String userName) {

        Boolean exists = this.service.existsByUserName(userName);

        return ResponseEntity.ok().body(exists);
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

    @PatchMapping()
    public ResponseEntity<UserDTO> update(@RequestBody UpdateUserRequestDTO userDTO) {

        UserDTO userUpdated = this.service.updateUser(userDTO);

        return ResponseEntity.ok(userUpdated);
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete () {

        this.service.delete();

        return ResponseEntity.noContent().build();
    }
}
