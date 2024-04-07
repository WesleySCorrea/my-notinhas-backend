package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentRequestDTO {
    @NotNull
    private String content;
    private LocalDateTime date;
    private Posts post;
    private Users user;
    private Comments parentComment;

    public Comments converterCommentRequestToComment () {

        Comments comment = new Comments();
        comment.setContent(this.getContent());
        comment.setDate(this.getDate());
        comment.setPost(this.getPost());
        comment.setUser(this.getUser());
        comment.setParentComment(this.getParentComment());
        comment.setActive(Boolean.TRUE);
        return comment;
    }
}