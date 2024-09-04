package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.request.CommunityUpdateRequestDTO;
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

    private final CommunityService communityService;

    @GetMapping
    public ResponseEntity<Page<CommunityResponseDTO>> findAllPublic(Pageable pageable) {

        Page<CommunityResponseDTO> communities = this.communityService.findAll(pageable);

        return ResponseEntity.ok().body(communities);
    }

    @GetMapping("/user")
    public ResponseEntity<Page<CommunityResponseDTO>> findAllCommunityByUser(Pageable pageable) {

        Page<CommunityResponseDTO> communities = this.communityService.findAllCommunityByUser(pageable);

        return ResponseEntity.ok().body(communities);

    }    @GetMapping("/owner")
    public ResponseEntity<Page<CommunityResponseDTO>> findAllCommunityByOwner(Pageable pageable) {

        Page<CommunityResponseDTO> communities = this.communityService.findAllCommunityByOwner(pageable);

        return ResponseEntity.ok().body(communities);
    }

    @PostMapping
    public ResponseEntity<CommunityResponseDTO> save(@RequestBody CommunityRequestDTO communityRequestDTO) {

        var community = this.communityService.save(communityRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(community);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponseDTO> findById(@PathVariable Long id) {

        CommunityResponseDTO community = this.communityService.findById(id);

        return ResponseEntity.ok().body(community);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody CommunityUpdateRequestDTO communityUpdateRequestDTO, @PathVariable Long id) {

        this.communityService.updateCommunity(communityUpdateRequestDTO, id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/entry/{communityId}")
    public ResponseEntity<Void> entryCommunity(@PathVariable Long communityId) {

        this.communityService.entryCommunity(communityId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/entry/private/{communityId}/{interestingUserId}/{approval}")
    public ResponseEntity<Void> entryPrivateCommunity(@PathVariable Long communityId,
                                                      @PathVariable Long interestingUserId,
                                                      @PathVariable Boolean approval) {

        this.communityService.entryPrivateCommunity(communityId, interestingUserId, approval);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        this.communityService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
