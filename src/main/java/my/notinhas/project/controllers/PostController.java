package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.PostResponseDTO;
import my.notinhas.project.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService service;

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> findAll(Pageable pageable) {

        Page<PostResponseDTO> posts = this.service.findAll(pageable);

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
