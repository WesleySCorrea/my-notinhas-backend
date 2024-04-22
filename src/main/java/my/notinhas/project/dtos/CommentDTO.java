package my.notinhas.project.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Comments;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentDTO {

    private Long id;
    private LocalDateTime date;
    private String content;
    private Boolean isEdited = Boolean.FALSE;
    private Boolean active;
    private PostDTO post;
    private UserDTO user;
    private CommentDTO parentComment;
    private List<CommentDTO> replies;

    public Comments toComment() {
        Comments comment = new Comments();
        comment.setId(this.getId());
        comment.setDate(this.getDate());
        comment.setContent(this.getContent());
        comment.setIsEdited(this.getIsEdited());
        comment.setActive(this.getActive());
        if (this.getParentComment() != null) {
            comment.setPost(this.getPost().convertPostDTOToPost());
        }
        if (this.getParentComment() != null) {
            comment.setUser(this.getUser().convertUserDTOToUser());
        }

        return comment;
    }
}
