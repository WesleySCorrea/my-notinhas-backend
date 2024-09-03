package my.notinhas.project.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.*;
import my.notinhas.project.enums.ActionEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotifyDTO {
    private Users notifyOwner;
    private Users user;
    private Posts post;
    private Comments comment;
    private Long parentId;
    private Community community;
    private ActionEnum actionEnum;
    private Boolean verified = Boolean.FALSE;
    private LocalDateTime date;

    public Notify converterNotifyDTOToNotify() {
        Notify notify = new Notify();
        notify.setNotifyOwner(this.getNotifyOwner());
        notify.setUser(this.getUser());
        notify.setPost(this.getPost());
        notify.setComment(this.getComment());
        notify.setParentId(this.getParentId());
        notify.setCommunity(this.getCommunity());
        notify.setActionEnum(this.getActionEnum());
        notify.setVerified(this.getVerified());
        notify.setDate(this.getDate());
        return notify;
    }
}