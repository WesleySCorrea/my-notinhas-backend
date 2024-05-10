package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.CommentDTO;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Comments;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentRequestDTO {
    @NotNull
    private String content;
    private PostDTO post;
    private CommentDTO parentComment;

    public Comments converterCommentRequestToComment (LocalDateTime date, UserDTO user) {

        Comments comment = new Comments();
        comment.setContent(this.getContent());
        comment.setDate(date);
        comment.setPost(this.getPost().convertPostDTOToPost());
        comment.setUser(user.convertUserDTOToUser());
        if (this.getParentComment() != null) {
            comment.setParentComment(this.getParentComment().toComment());
        }
        comment.setActive(Boolean.TRUE);

        return comment;
    }
}