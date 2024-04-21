package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.LikeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LikeRequestDTO {
    @NotNull
    private LikeEnum likeEnum;
    private PostDTO post;

    public Likes converterLikeRequestToLike (UserDTO user) {

        Likes like = new Likes(
                null,
                LocalDateTime.now(),
                this.getLikeEnum(),
                this.getPost().convertPostDTOToPost(),
                user.convertUserDTOToUser()
        );
        return like;
    }
}