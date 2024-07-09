package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.enums.LikeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostResponseDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime date;
    private String content;
    private UserPostResponseDTO user;
    private Long totalLikes;
    private Long totalComments;
    private LikeEnum userLike;
    private Boolean postOwner;
    private Boolean isEdited;

    public PostResponseDTO(Posts post) {
        this.id = post.getId();
        this.date = post.getDate();
        this.content = post.getContent();
        this.user = new UserPostResponseDTO(post.getUser().getId(), post.getUser().getUserName());
        this.isEdited = post.getIsEdited();
    }
}