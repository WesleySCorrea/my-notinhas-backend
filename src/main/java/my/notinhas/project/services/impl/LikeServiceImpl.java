package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.LikeRequestDTO;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.ActionEnum;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.LikeRepository;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.LikeService;
import my.notinhas.project.services.NotifyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository repository;
    private final NotifyService notifyService;

    @Override
    public void saveLike(LikeRequestDTO likeRequestDTO) {
        UserDTO userDTO = ExtractUser.get();

        Likes existingLike = repository.findByUserIdAndPostId(userDTO.getUserId(), likeRequestDTO.getPost().getId());

        if (existingLike != null) {

            if (existingLike.getLikeEnum() == likeRequestDTO.getLikeEnum()) {

                this.repository.deleteById(existingLike.getId());

            } else {
                existingLike.setLikeEnum(likeRequestDTO.getLikeEnum());
                existingLike.setDate(LocalDateTime.now());
                this.repository.save(existingLike);
            }

        } else {

            Likes request = likeRequestDTO.converterLikeRequestToLike(userDTO);

            try {
                Likes likePersisted = this.repository.save(request);

                this.createNotification(likePersisted, userDTO, likeRequestDTO.getPost().getUserId());

            } catch (Exception e) {
                throw new PersistFailedException("Fail when the like was persisted");
            }
        }
    }

    private void createNotification(Likes like, UserDTO userDTO, Long userId) {

        Users notifyOwner = new Users();
        notifyOwner.setId(userId);

        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setNotifyOwner(notifyOwner);
        notifyDTO.setUser(userDTO.convertUserDTOToUser());
        notifyDTO.setPost(like.getPost());
        notifyDTO.setActionEnum(ActionEnum.LIKE_POST);
        notifyDTO.setVerified(Boolean.FALSE);
        notifyDTO.setDate(LocalDateTime.now());

        this.notifyService.saveNotify(notifyDTO);
    }
}