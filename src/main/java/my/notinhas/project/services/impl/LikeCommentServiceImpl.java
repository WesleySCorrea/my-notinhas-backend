package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.LikeCommentRequestDTO;
import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.ActionEnum;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.LikeCommentRepository;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.LikeCommentService;
import my.notinhas.project.services.NotifyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LikeCommentServiceImpl implements LikeCommentService {

    private final LikeCommentRepository repository;
    private final NotifyService notifyService;

    @Override
    public void saveLike(LikeCommentRequestDTO likeCommentRequestDTO) {
        UserDTO userDTO = ExtractUser.get();

        LikesComments existingLike = repository.findByUserIdAndCommentId(userDTO.getUserId(), likeCommentRequestDTO.getComment().getId());

        if (existingLike != null) {

            if (existingLike.getLikeEnum() == likeCommentRequestDTO.getLikeEnum()) {

                this.repository.deleteById(existingLike.getId());

            } else {
                existingLike.setLikeEnum(likeCommentRequestDTO.getLikeEnum());
                existingLike.setDate(LocalDateTime.now());
                this.repository.save(existingLike);
            }

        } else {

            LikesComments request = likeCommentRequestDTO.converterLikeCommentRequestToLike(userDTO);

            try {
                LikesComments likePersisted = this.repository.save(request);

                this.createNotification(likePersisted,
                        userDTO,
                        likeCommentRequestDTO.getComment().getPost().getUserId(),
                        likeCommentRequestDTO.getComment().getPost().getId());
            } catch (Exception e) {
                throw new PersistFailedException("Fail when the like was persisted");
            }
        }
    }

    private void createNotification(LikesComments like, UserDTO userDTO, Long userId, Long postId) {

        Users notifyOwner = new Users();
        notifyOwner.setId(userId);

        Posts postOwner = new Posts();
        postOwner.setId(postId);


        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setNotifyOwner(notifyOwner);
        notifyDTO.setUser(userDTO.convertUserDTOToUser());
        notifyDTO.setPost(postOwner);
        notifyDTO.setActionEnum(ActionEnum.LIKE_COMMENT);
        notifyDTO.setVerified(Boolean.FALSE);
        notifyDTO.setDate(LocalDateTime.now());

        this.notifyService.saveNotify(notifyDTO);
    }

}