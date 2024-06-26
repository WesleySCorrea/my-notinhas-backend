package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.LikeCommentRequestDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.ActionEnum;
import my.notinhas.project.enums.LikeEnum;
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

                this.removeNotification(likeCommentRequestDTO, likeCommentRequestDTO.getComment().getPost().getUserId(),
                        likeCommentRequestDTO.getComment().getPost().getId(),
                        userDTO.getUserId());
            } else {
                existingLike.setLikeEnum(likeCommentRequestDTO.getLikeEnum());
                existingLike.setDate(LocalDateTime.now());
                this.repository.save(existingLike);

                this.modifyNotification(likeCommentRequestDTO, userDTO);
            }
        } else {

            LikesComments request = likeCommentRequestDTO.converterLikeCommentRequestToLike(userDTO);

            try {
                LikesComments likePersisted = this.repository.save(request);

                    this.createNotification(likePersisted, userDTO, likeCommentRequestDTO);
            } catch (Exception e) {
                throw new PersistFailedException("Fail when the like was persisted");
            }
        }
    }

    private void removeNotification(LikeCommentRequestDTO like, Long ownerUserId, Long postId, Long userId) {

        ActionEnum action = this.verifyActionEnum(like.getLikeEnum());
        this.notifyService.removeNotification(ownerUserId, userId, postId, action);
    }

    private void createNotification(LikesComments like, UserDTO userDTO, LikeCommentRequestDTO likeRequest) {

        ActionEnum actionEnum = this.verifyActionEnum(like.getLikeEnum());

        Users notifyOwner = new Users();
        notifyOwner.setId(likeRequest.getComment().getPost().getUserId());

        Posts postOwner = new Posts();
        postOwner.setId(likeRequest.getComment().getPost().getId());

        Comments comments = new Comments();
        comments.setId(likeRequest.getComment().getId());


        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setNotifyOwner(notifyOwner);
        notifyDTO.setUser(userDTO.convertUserDTOToUser());
        notifyDTO.setPost(postOwner);
        notifyDTO.setComment(comments);
        notifyDTO.setActionEnum(actionEnum);
        notifyDTO.setVerified(Boolean.FALSE);
        notifyDTO.setDate(LocalDateTime.now());

        this.notifyService.saveNotify(notifyDTO);
    }

    private void modifyNotification(LikeCommentRequestDTO like, UserDTO userDTO) {

        ActionEnum newActionEnum = this.verifyActionEnum(like.getLikeEnum());
        ActionEnum currentActionEnum = this.verifyCurrentActionEnum(like.getLikeEnum());

        this.notifyService.updateNotificationComment(like.getComment().getPost().getUserId(),
                userDTO.getUserId(),
                like.getComment().getPost().getId(),
                like.getComment().getId(),
                newActionEnum,
                currentActionEnum);
    }


    private ActionEnum verifyActionEnum(LikeEnum likeEnum) {

        ActionEnum actionEnum;
        if (likeEnum == LikeEnum.LIKE) {
            actionEnum = ActionEnum.LIKE_COMMENT;
        } else {
            actionEnum = ActionEnum.DISLIKE_COMMENT;
        }
        return actionEnum;
    }

    private ActionEnum verifyCurrentActionEnum(LikeEnum likeEnum) {

        ActionEnum actionEnum;
        if (likeEnum == LikeEnum.LIKE) {
            actionEnum = ActionEnum.DISLIKE_COMMENT;
        } else {
            actionEnum = ActionEnum.LIKE_COMMENT;
        }
        return actionEnum;
    }
}