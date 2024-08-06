package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.PostRequestDTO;
import my.notinhas.project.dtos.response.PostPublicResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import my.notinhas.project.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService service;

    @GetMapping("/public")
    public ResponseEntity<Page<PostPublicResponseDTO>> findAllPublic(Pageable pageable) {

        Page<PostPublicResponseDTO> posts = this.service.findAllPublic(pageable);

        return ResponseEntity.ok().body(posts);
    }

    @GetMapping()
    public ResponseEntity<Page<PostResponseDTO>> findAll(Pageable pageable) {

        Page<PostResponseDTO> posts = this.service.findAll(pageable);

        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable Long id) {

        PostResponseDTO post = this.service.findByID(id);

        return ResponseEntity.ok().body(post);
    }

    @GetMapping("/search/{content}")
    public ResponseEntity<Page<PostResponseDTO>> searchPosts(Pageable pageable, @PathVariable String content) {

        return ResponseEntity.ok().body(this.service.searchPosts(pageable, content));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody PostRequestDTO postRequestDTO) {

        this.service.savePost(postRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody PostRequestDTO postRequestDTO, @PathVariable Long id) {

        this.service.updatePost(postRequestDTO, id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {

        this.service.deleteByID(id);

        return ResponseEntity.noContent().build();
    }
}
