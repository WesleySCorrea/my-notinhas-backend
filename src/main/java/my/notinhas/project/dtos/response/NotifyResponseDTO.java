package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.Notify;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.ActionEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotifyResponseDTO {

    private Long id;
    private Long postId;
    private Long commentId;
    private Long userId;
    private String userName;
    private ActionEnum actionEnum;
    private Boolean verified;
    private String date;

    public NotifyResponseDTO converterNotifyToNotifyResponseDTO(Notify notify) {

        NotifyResponseDTO notifyResponse = new NotifyResponseDTO();

        notifyResponse.setId(notify.getId());
        if (notify.getPost() != null) {
            notifyResponse.setPostId(notify.getPost().getId());
        }
        if (notify.getComment() != null) {
            notifyResponse.setCommentId(notify.getComment().getId());
        }
        notifyResponse.setUserId(notify.getUser().getId());
        notifyResponse.setUserName(notify.getUser().getUserName());
        notifyResponse.setActionEnum(notify.getActionEnum());
        notifyResponse.setVerified(notify.getVerified());
        notifyResponse.setDate(String.valueOf(notify.getDate()));

        return notifyResponse;
    }
}
