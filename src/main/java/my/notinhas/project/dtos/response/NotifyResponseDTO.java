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
    private Users user;
    private Posts post;
    private Comments comment;
    private ActionEnum actionEnum;
    private Boolean verified;
    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
    private LocalDateTime date;

    public NotifyResponseDTO converterNotifyToNotifyResponseDTO(Notify notify) {

        NotifyResponseDTO notifyResponse = new NotifyResponseDTO();

        notifyResponse.setId(notify.getId());
        notifyResponse.setUser(notify.getNotifyOwner());
        notifyResponse.setPost(notify.getPost());
        notifyResponse.setComment(notify.getComment());
        notifyResponse.setActionEnum(notify.getActionEnum());
        notifyResponse.setVerified(notify.getVerified());
        notifyResponse.setDate(notify.getDate());

        return notifyResponse;
    }
}
