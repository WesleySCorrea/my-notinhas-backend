package my.notinhas.project.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostDTO {
    private Long id;
    private String userName;
    private LocalDate date;
    private String content;
    private Boolean active;
    private Users user;
    private Long totalLikes;
    private List<LikeDTO> likes = new ArrayList<>();

    public void calculateTotalLikesAndDeslikes(List<LikeDTO> likes) {
        this.totalLikes = 0L;

        for (LikeDTO like : likes) {
            if (like.getValue() == Value.LIKE) {
                this.totalLikes++;
            } else if (like.getValue() == Value.DESLIKE) {
                this.totalLikes--;
            }
        }
    }
}