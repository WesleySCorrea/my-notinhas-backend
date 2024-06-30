package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.response.NotifyResponseDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.Notify;
import my.notinhas.project.enums.ActionEnum;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.NotifyRepository;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.NotifyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final NotifyRepository notifyRepository;

    @Override
    public void saveNotify(NotifyDTO notify, Long userId) {

        if(!notify.getNotifyOwner().getId().equals(userId)) {
            try {
                this.notifyRepository.save(notify.converterNotifyDTOToNotify());
            } catch (Exception e) {
                throw new PersistFailedException("Fail when the object was persisted");
            }
        }
    }

    @Override
    @Transactional
    public void removeNotificationOfLikePost(Long notifyOwnerId, Long userId, Long postId, ActionEnum action) {

        this.notifyRepository.deleteByNotifyOwnerIdAndUserIdAndActionEnumAndPostId(notifyOwnerId, userId, action, postId);
    }

    @Override
    @Transactional
    public void removeNotificationOfLikeComment(Long commentId, Long notifyOwnerId, Long userId, Long postId, ActionEnum action) {

        this.notifyRepository.deleteByCommentIdAndNotifyOwnerIdAndUserIdAndActionEnumAndPostId(commentId, notifyOwnerId, userId, action, postId);
    }

    @Override
    @Transactional
    public void removeNotificationOfComment(Comments comments, Long userId, ActionEnum action) {

        Long commentId = comments.getId();
        Long postId = comments.getPost().getId();

        this.notifyRepository.deleteByCommentIdAndUserIdAndActionEnumAndPostId(
                commentId,
                userId,
                action,
                postId
        );
    }

    @Override
    @Transactional
    public void updateNotificationLikePost(Long notifyOwnerId, Long userId, Long postId, ActionEnum newAction, ActionEnum currentAction) {

        this.notifyRepository.updateActionEnumByNotifyOwnerIdAndUserIdAndPostId(newAction, notifyOwnerId, userId, postId, currentAction);
    }

    @Override
    @Transactional
    public void updateNotificationLikeComment(Long notifyOwnerId, Long userId, Long postId, Long commentId, ActionEnum newAction, ActionEnum currentAction) {

        this.notifyRepository.updateActionEnumByNotifyOwnerIdAndUserIdAndPostIdAndCommentId(newAction, notifyOwnerId, userId, postId, commentId, currentAction);
    }

    @Override
    public Page<NotifyResponseDTO> findAllNotifications(Pageable pageable) {
        UserDTO user = ExtractUser.get();

        Page<Notify> notifies = this.notifyRepository.findAllByNotifyOwnerIdAndVerifiedFalseOrderByDateDesc(user.getUserId(), pageable);

        List<NotifyResponseDTO> notifyResponseDTOS = notifies.stream()
                .map(notify -> {
                    NotifyResponseDTO notifyDTO = new NotifyResponseDTO();
                    return notifyDTO.converterNotifyToNotifyResponseDTO(notify);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(notifyResponseDTOS, pageable, notifies.getTotalElements());
    }

    @Override
    public void verifyNotify(Long id) {
        UserDTO user = ExtractUser.get();

        this.notifyRepository.updateVerifiedByIdAndNotifyOwnerId(id, user.getUserId());
    }
}