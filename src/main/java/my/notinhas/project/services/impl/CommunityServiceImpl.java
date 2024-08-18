package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import my.notinhas.project.entities.CommunityMember;
import my.notinhas.project.enums.RoleEnum;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.repositories.CommunityMemberRepository;
import my.notinhas.project.repositories.CommunityRepository;
import my.notinhas.project.services.CommunityService;
import my.notinhas.project.services.ExtractUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository repository;
    private final CommunityMemberRepository memberRepository;


    @Override
    public Page<CommunityResponseDTO> findAll(Pageable pageable) {
        var communities = repository.findAllPCommunities(pageable);
        return new CommunityResponseDTO()
                .convertToCommunityResponseDTO(communities);
    }

    @Override
    public CommunityResponseDTO save(CommunityRequestDTO communityRequestDTO) {
        UserDTO user = ExtractUser.get();
        var entity = communityRequestDTO.toEntity(user);
        var result = repository.save(entity);
        var communityMember = new CommunityMember();
        communityMember.setCommunity(result);
        communityMember.setRole(RoleEnum.OWNER);
        communityMember.setUser(user.convertUserDTOToUser());
        communityMember.setJoinedDate(LocalDateTime.now());
        this.memberRepository.save(communityMember);
        return new CommunityResponseDTO(result);
    }
    @Override
    public CommunityResponseDTO findById(Long id) {
        var community = repository.findById(id);
        if (community.isEmpty()) {
            throw new ObjectNotFoundException("Post not found or update failed");
        }
        return new CommunityResponseDTO(community.get());
    }
}
