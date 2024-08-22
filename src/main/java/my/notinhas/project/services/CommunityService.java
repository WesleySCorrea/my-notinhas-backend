package my.notinhas.project.services;

import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.request.CommunityUpdateRequestDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityService {

    Page<CommunityResponseDTO> findAll(Pageable pageable);
    Page<CommunityResponseDTO> findAllCommunityByUser(Pageable pageable);
    Page<CommunityResponseDTO> findAllCommunityByOwner(Pageable pageable);
    CommunityResponseDTO save(CommunityRequestDTO communityRequestDTO);
    CommunityResponseDTO findById(Long id);
    void updateCommunity(CommunityUpdateRequestDTO communityUpdateRequestDTO, Long id);

}
