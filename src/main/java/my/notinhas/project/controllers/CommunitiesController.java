package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import my.notinhas.project.services.CommunityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/communities")
public class CommunitiesController {

    private final CommunityService service;

    @GetMapping
    public ResponseEntity<Page<CommunityResponseDTO>> findAllPublic(Pageable pageable) {

        Page<CommunityResponseDTO> posts = this.service.findAll(pageable);

        return ResponseEntity.ok().body(posts);
    }

    @PostMapping
    public ResponseEntity<CommunityResponseDTO> save(@RequestBody CommunityRequestDTO communityRequestDTO) {

        var response = this.service.save(communityRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
