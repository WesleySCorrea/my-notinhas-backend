package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CommentResponseDTO {
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;
    private String content;
    private UserPostResponseDTO user;
    private List<CommentResponseDTO> replies;

    public CommentResponseDTO converterCommentToCommentResponse(Comments comment) {

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setDate(comment.getDate());
        commentResponseDTO.setContent(comment.getContent());
        commentResponseDTO.setUser(new UserPostResponseDTO(comment.getUser().getUserName()));

        List<CommentResponseDTO> replies = comment.getReplies().stream()
                .map(comments -> {
                    return new CommentResponseDTO()
                            .converterCommentToCommentResponse(comments);
                })
                .toList();

        commentResponseDTO.setReplies(replies);

        return commentResponseDTO;
    }
}