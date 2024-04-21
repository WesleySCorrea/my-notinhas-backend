package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.enums.LikeEnum;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentResponseDTO {
    private Long id;
    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
    private LocalDateTime date;
    private String content;
    private Boolean isEdited;
    private Long totalLikes;
    private Integer totalReplies;
    private LikeEnum userLike;
    private Boolean commentOwner;
    private UserPostResponseDTO user;
    private List<CommentResponseDTO> replies;

    public CommentResponseDTO converterCommentToCommentResponse(Comments comment, UserDTO user) {

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setDate(comment.getDate());
        commentResponseDTO.setContent(comment.getContent());
        commentResponseDTO.setIsEdited(comment.getIsEdited());
        Integer sizes = 0;
        if (comment.getReplies() != null) {
            for(Comments comments : comment.getReplies()) {
                if (comments.getActive()) sizes++;
            }
        }
        commentResponseDTO.setTotalReplies(sizes);
        if (comment.getUser().getUserName().equals(user.getUserName())) {
            commentResponseDTO.setCommentOwner(Boolean.TRUE);
        } else commentResponseDTO.setCommentOwner(Boolean.FALSE);

        commentResponseDTO.setUser(new UserPostResponseDTO(comment.getUser().getUserName()));

        List<CommentResponseDTO> replies = comment.getReplies().stream()
                .map(comments -> new CommentResponseDTO()
                        .converterCommentToCommentResponse(comments, user))
                .toList();

        commentResponseDTO.setReplies(replies);

        return commentResponseDTO;
    }
}