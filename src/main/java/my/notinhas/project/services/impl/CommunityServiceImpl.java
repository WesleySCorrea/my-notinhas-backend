package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommunityRequestDTO;
import my.notinhas.project.dtos.request.CommunityUpdateRequestDTO;
import my.notinhas.project.dtos.response.CommunityResponseDTO;
import my.notinhas.project.entities.Community;
import my.notinhas.project.entities.CommunityMember;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.ActionEnum;
import my.notinhas.project.enums.RoleEnum;
import my.notinhas.project.enums.StatusMemberEnum;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.repositories.CommunityMemberRepository;
import my.notinhas.project.repositories.CommunityRepository;
import my.notinhas.project.services.CommunityService;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.NotifyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static my.notinhas.project.mapper.CommunityMemberMapper.createCommunityMember;

@Service
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final NotifyService notifyService;
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;


    @Override
    public Page<CommunityResponseDTO> findAll(Pageable pageable) {
        UserDTO user = ExtractUser.get();

        var communities = communityRepository.findAllPCommunities(user.getUserId(), pageable);
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
        communityMember.setRoleMember(RoleEnum.OWNER);
        communityMember.setUser(user.convertUserDTOToUser());
        communityMember.setJoinedDate(LocalDateTime.now());
        communityMember.setStatusMember(StatusMemberEnum.OWNER);
        this.memberRepository.save(communityMember);
        return new CommunityResponseDTO(result);
    }

    @Override
    public CommunityResponseDTO findById(Long communityId) {

        UserDTO user = ExtractUser.get();

        List<Object[]> communities = this.communityRepository.findByIdAndActiveTrue(communityId, user.getUserId());

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

    @Override
    public void entryCommunity(Long communityId) {

        UserDTO user = ExtractUser.get();

        Optional<Community> community = this.communityRepository.findById(communityId);

        if (community.isEmpty()) {
            throw new ObjectNotFoundException("Community not found");
        } else if (community.get().getProtectedCommunity()) {

            this.entryPrivateCommunity(community.get().getId(), user.getUserId(), community.get().getOwner().getId());

        } else {

            var communityMember = createCommunityMember(communityId, user.getUserId(), StatusMemberEnum.JOINED);

            try {
                this.memberRepository.save(communityMember);
            } catch (Exception e) {
                throw new PersistFailedException("Error while joining community");
            }
        }
    }

    private void entryPrivateCommunity(Long communityId, Long userId, Long ownerCommunityId) {

        this.createNotification(communityId, ownerCommunityId, ExtractUser.get());

        this.memberRepository.save(createCommunityMember(communityId, userId, StatusMemberEnum.PENDING));

    }

    @Override
    public void entryPrivateCommunity(Long communityId, Long interestingUserId, Boolean approval) {
        UserDTO user = ExtractUser.get();
        var community = this.findById(communityId);

        if (community.getOwner().getUserId().equals(user.getUserId())) {

            if (approval) {
                this.memberRepository.updateStatusMemberByCommunityIdAndUserId(communityId, interestingUserId);
            } else {
                this.memberRepository.deleteByCommunityIdAndUserIdAndStatusMember(communityId, interestingUserId, StatusMemberEnum.PENDING);
            }
        } else throw new PersistFailedException("Community does not belong to the user");
    }

    @Override
    public void delete(Long id) {

        UserDTO user = ExtractUser.get();

        CommunityResponseDTO communityResponseDTO = this.findById(id);
        if (communityResponseDTO.getOwner().getUserId().equals(user.getUserId())) {

            this.communityRepository.updateCommunityByIdToFalse(id, user.getUserId());
        }
    }

    private void createNotification(Long communityId, Long notifyOwnerId, UserDTO userDTO) {

        ActionEnum actionEnum = ActionEnum.ENTRY_INTO_COMMUNITY;

        Users notifyOwner = new Users();
        notifyOwner.setId(notifyOwnerId);

        Community community = new Community();
        community.setId(communityId);

        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setNotifyOwner(notifyOwner);
        notifyDTO.setUser(userDTO.convertUserDTOToUser());
        notifyDTO.setCommunity(community);
        notifyDTO.setActionEnum(actionEnum);
        notifyDTO.setVerified(Boolean.FALSE);
        notifyDTO.setDate(LocalDateTime.now());

        this.notifyService.saveNotify(notifyDTO, userDTO.getUserId());
    }
}