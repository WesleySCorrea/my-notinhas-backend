package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.Posts;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentRequestDTO {
    @NotNull
    private String content;
    private Long postId;
    private Long commentUserId;
    private Long postOwnerId;
    private Long parentCommentId;

    public Comments converterCommentRequestToComment (LocalDateTime date, UserDTO user) {

        Posts post = new Posts();
        post.setId(this.getPostId());

        Comments comment = new Comments();
        comment.setContent(this.getContent());
        comment.setDate(date);
        comment.setPost(post);
        comment.setUser(user.convertUserDTOToUser());
        if (this.getParentCommentId() != null) {
            Comments parentComment = new Comments();
            parentComment.setId(this.getParentCommentId());
            comment.setParentComment(parentComment);
        }
        comment.setActive(Boolean.TRUE);

        return comment;
    }
}