package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.request.LikeRequestDTO;
import my.notinhas.project.services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService service;


    @PostMapping
    public ResponseEntity<LikeDTO> save(@RequestBody LikeRequestDTO likeRequestDTO) {

        LikeDTO like = this.service.saveLike(likeRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }
}