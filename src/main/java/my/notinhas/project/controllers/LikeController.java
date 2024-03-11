package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.LikeResponseDTO;
import my.notinhas.project.services.ILikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final ILikeService service;

    @GetMapping
    public ResponseEntity<List<LikeResponseDTO>> findAll() {

        List<LikeResponseDTO> likes = this.service.findAll();

        return ResponseEntity.ok().body(likes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LikeResponseDTO> findById(@PathVariable Long id) {

        LikeResponseDTO like = this.service.findByID(id);

        return ResponseEntity.ok().body(like);
    }

    @PostMapping
    public ResponseEntity<LikeDTO> save(@RequestBody LikeDTO likeDTO) {

        LikeDTO like = this.service.saveLike(likeDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LikeDTO> update(@RequestBody LikeDTO likeDTO, @PathVariable Long id) {

        LikeDTO likeUpdated = this.service.updateLike(likeDTO, id);

        return ResponseEntity.ok(likeUpdated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {

        this.service.deleteByID(id);

        return ResponseEntity.noContent().build();
    }
}
