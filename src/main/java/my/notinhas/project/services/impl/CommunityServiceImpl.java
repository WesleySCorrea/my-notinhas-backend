package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import my.notinhas.project.repositories.CommunityRepository;
import my.notinhas.project.services.CommunityService;
import my.notinhas.project.services.ExtractUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository repository;


    @Override
    public Page<CommunityResponseDTO> findAll(Pageable pageable) {
        var communities = repository.findAll(pageable);
        return communities.map(CommunityResponseDTO::new);
    }

    @Override
    public CommunityResponseDTO save(CommunityRequestDTO communityRequestDTO) {
        UserDTO user = ExtractUser.get();
        var entity = communityRequestDTO.toEntity(user.getUserId());
        var result = repository.save(entity);
        return new CommunityResponseDTO(result);
    }
}
