package my.notinhas.project.services;

import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityService {

    Page<CommunityResponseDTO> findAll(Pageable pageable);

    CommunityResponseDTO save(CommunityRequestDTO communityRequestDTO);

}
