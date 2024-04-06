package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Comments;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentToUserDTO {
    private Long id;
    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
    private LocalDateTime date;
    private String content;
    private Long postId;
    private String postContent;
    private Boolean isEdited;
    private Integer totalReplies;

    public CommentToUserDTO converterCommentToCommentToUser(Comments comment) {

        CommentToUserDTO commentResponseDTO = new CommentToUserDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setDate(comment.getDate());
        commentResponseDTO.setContent(comment.getContent());
        commentResponseDTO.setPostId(comment.getPost().getId());
        commentResponseDTO.setPostContent(comment.getPost().getContent());
        commentResponseDTO.setIsEdited(comment.getIsEdited());
        commentResponseDTO.setTotalReplies(comment.getReplies().size());

        return commentResponseDTO;
    }
}