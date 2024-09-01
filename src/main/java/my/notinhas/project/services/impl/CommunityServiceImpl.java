package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.request.CommunityUpdateRequestDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import my.notinhas.project.entities.CommunityMember;
import my.notinhas.project.enums.RoleEnum;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
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
    public Page<CommunityResponseDTO> findAllCommunityByUser(Pageable pageable, Long userId) {
        Page<Object[]> communities = communityRepository.findAllCommunityByUser(userId, pageable);

        List<CommunityResponseDTO> communityResponseDTO = communities.stream()
                .map(CommunityResponseDTO::new)
                .toList();

        return new PageImpl<>(communityResponseDTO, pageable, communities.getTotalElements());
    }

    @Override
    public Page<CommunityResponseDTO> findAllCommunityByOwner(Pageable pageable) {
        UserDTO user = ExtractUser.get();
        Page<Object[]> communities = communityRepository.findAllCommunityByOwner(user.getUserId(), pageable);

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
    public CommunityResponseDTO findById(Long communityId) {

        List<Object[]> communities = this.communityRepository.findByIdAndActiveTrue(communityId);

        var communityResponseDTO = communities.stream()
                .map(CommunityResponseDTO::new)
                .toList();

        return communityResponseDTO.get(0);
    }

    @Override
    public void updateCommunity(CommunityUpdateRequestDTO communityUpdateRequestDTO, Long id) {
        UserDTO user = ExtractUser.get();

        CommunityResponseDTO communityPersisted = this.findById(id);

        if (communityPersisted.getOwner().getUserId().equals(user.getUserId())) {

            int updatedRows = communityRepository.updateCommunityDescription(
                    communityPersisted.getId(),
                    communityUpdateRequestDTO.getDescription()
            );

            if (updatedRows == 0) {
                throw new ObjectNotFoundException("Community not found or update failed");
            }
        } else {
            throw new UnauthorizedIdTokenException("Community does not belong to the user");
        }
    }
}