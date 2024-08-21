package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.response.CommentResponseDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import my.notinhas.project.entities.CommunityMember;
import my.notinhas.project.enums.RoleEnum;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.repositories.CommunityMemberRepository;
import my.notinhas.project.repositories.CommunityRepository;
import my.notinhas.project.services.CommunityService;
import my.notinhas.project.services.ExtractUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;


    @Override
    public Page<CommunityResponseDTO> findAll(Pageable pageable) {
        var communities = communityRepository.findAllPCommunities(pageable);
        return new CommunityResponseDTO()
                .convertToCommunityResponseDTO(communities);
    }

    @Override
    public Page<CommunityResponseDTO> findAllCommunityByUser(Pageable pageable) {
        UserDTO user = ExtractUser.get();
        Page<Object[]> communities = communityRepository.findAllCommunityByUser(user.getUserId(), pageable);

        List<CommunityResponseDTO> communityResponseDTO = communities.stream()
                .map(CommunityResponseDTO::new)
                .toList();

        return new PageImpl<>(communityResponseDTO, pageable, communities.getTotalElements());
    }

    @Override
    public CommunityResponseDTO save(CommunityRequestDTO communityRequestDTO) {
        UserDTO user = ExtractUser.get();
        var entity = communityRequestDTO.toEntity(user);
        var result = communityRepository.save(entity);
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
        var community = communityRepository.findById(id);
        if (community.isEmpty()) {
            throw new ObjectNotFoundException("Post not found or update failed");
        }
        return new CommunityResponseDTO(community.get());
    }
}
