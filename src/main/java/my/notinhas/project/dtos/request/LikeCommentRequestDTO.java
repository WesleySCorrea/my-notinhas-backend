package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.CommentDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.enums.LikeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LikeCommentRequestDTO {
    @NotNull
    private LikeEnum likeEnum;
    private CommentDTO comment;

    public LikesComments converterLikeCommentRequestToLike (UserDTO user) {

        LikesComments like = new LikesComments(
                null,
                LocalDateTime.now(),
                this.getLikeEnum(),
                this.getComment().toComment(),
                user.convertUserDTOToUser()
        );
        return like;
    }
}