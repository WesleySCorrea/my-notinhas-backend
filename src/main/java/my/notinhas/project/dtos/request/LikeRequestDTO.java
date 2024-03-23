package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.LikeEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LikeRequestDTO {
    @NotNull
    private LikeEnum likeEnum;
    private Posts post;
    private Users user;

    public Likes converterLikeRequestToLike () {

        Likes like = new Likes(
                null,
                this.getLikeEnum(),
                this.getPost(),
                this.user
        );
        return like;
    }
}