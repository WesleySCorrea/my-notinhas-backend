package my.notinhas.project.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.LikeCommentRequestDTO;
import my.notinhas.project.dtos.request.LikeRequestDTO;
import my.notinhas.project.services.LikeCommentService;
import my.notinhas.project.services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/likecom")
public class LikeCommentController {

    private final LikeCommentService service;

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody LikeCommentRequestDTO likeCommentRequestDTO) {

        this.service.saveLike(likeCommentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}