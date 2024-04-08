package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.LikeEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LikeCommentRequestDTO {
    @NotNull
    private LikeEnum likeEnum;
    private Comments comment;
    private Users user;

    public LikesComments converterLikeCommentRequestToLike () {

        LikesComments like = new LikesComments(
                null,
                null,
                this.getLikeEnum(),
                this.getComment(),
                this.user
        );
        return like;
    }
}