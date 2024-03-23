package my.notinhas.project.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.LikeRequestDTO;
import my.notinhas.project.services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService service;

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody LikeRequestDTO likeRequestDTO) {

        this.service.saveLike(likeRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}