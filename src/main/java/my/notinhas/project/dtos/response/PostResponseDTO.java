package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.enums.LikeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostResponseDTO {
    private Long id;
    private LocalDateTime date;
    private String content;
    private UserPostResponseDTO user;
    private Long totalLikes;
    private Long totalComments;
    private LikeEnum userLike;
    private Boolean postOwner;
    private Boolean isEdited;
}