package my.notinhas.project.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.dtos.response.CommentResponseDTO;
import my.notinhas.project.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService service;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> findByPostId(@PathVariable Long postId) {

        List<CommentResponseDTO> comments = this.service.findByPostId(postId);

        return ResponseEntity.ok().body(comments);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {

        this.service.saveComment(commentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}