package my.notinhas.project.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostDTO {
    private Long id;
    private LocalDateTime date;
    private String content;
    private Boolean active;
    private UserDTO user;
    private Long totalLikes;
//    private List<LikeDTO> likes;
}