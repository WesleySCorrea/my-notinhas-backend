package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.PostResponseDTO;
import my.notinhas.project.services.IPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final IPostService service;

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> findAll() {

        List<PostResponseDTO> posts = this.service.findAll();

        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable Long id) {

        PostResponseDTO post = this.service.findByID(id);

        return ResponseEntity.ok().body(post);
    }

    @PostMapping
    public ResponseEntity<PostDTO> save(@RequestBody PostDTO postDTO) {

        PostDTO post = this.service.savePost(postDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostDTO> update(@RequestBody PostDTO postDTO, @PathVariable Long id) {

        PostDTO postUpdated = this.service.updatePost(postDTO, id);

        return ResponseEntity.ok(postUpdated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {

        this.service.deleteByID(id);

        return ResponseEntity.noContent().build();
    }
}
