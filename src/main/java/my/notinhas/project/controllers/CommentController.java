package my.notinhas.project.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.dtos.response.CommentResponseDTO;
import my.notinhas.project.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService service;

    @GetMapping("/son/{id}")
    public ResponseEntity<Page<CommentResponseDTO>> findAllCommentSon(@PathVariable Long id, Pageable pageable) {

        Page<CommentResponseDTO> comments = this.service.findAllCommentSon(pageable, id);

        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<CommentResponseDTO>> findByPostId(@PathVariable Long postId, Pageable pageable) {

        Page<CommentResponseDTO> comments = this.service.findByPostId(postId, pageable);

        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/notify/{postId}/{commentId}")
    public ResponseEntity<Page<CommentResponseDTO>> findCommentToNotify(@PathVariable Long postId, @PathVariable Long commentId, Pageable pageable) {

        Page<CommentResponseDTO> comments = this.service.findCommentToNotify(postId, commentId, pageable);

        return ResponseEntity.ok().body(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> save(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {

        var objectPersisted = this.service.saveComment(commentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(objectPersisted);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody CommentRequestDTO commentRequestDTO, @PathVariable Long id) {

        this.service.updateComment(commentRequestDTO, id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        this.service.delete(id);

        return ResponseEntity.noContent().build();
    }
}