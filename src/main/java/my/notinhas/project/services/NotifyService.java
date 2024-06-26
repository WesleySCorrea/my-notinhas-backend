package my.notinhas.project.services;

import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.response.NotifyResponseDTO;
import my.notinhas.project.enums.ActionEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotifyService {

    void saveNotify(NotifyDTO notify);
    void removeNotification(Long notifyOwnerId, Long userId, Long postId, ActionEnum action);
    void updateNotificationPost(Long notifyOwnerId, Long userId, Long postId,
                                   ActionEnum newAction, ActionEnum currentActionEnum);
    void updateNotificationComment(Long notifyOwnerId, Long userId, Long postId, Long commentId,
                            ActionEnum newAction, ActionEnum currentActionEnum);
    Page<NotifyResponseDTO> findAllNotifications(Pageable pageable);
}