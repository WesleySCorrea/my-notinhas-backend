package my.notinhas.project.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService service;

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {

        this.service.saveComment(commentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}