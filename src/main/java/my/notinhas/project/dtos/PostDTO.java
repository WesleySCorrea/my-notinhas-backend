package my.notinhas.project.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Posts;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostDTO {
    private Long id;
    private LocalDateTime date;
    private String content;
    private String userName;
    private Long userId;
    private Boolean active;
    private Boolean isEdited;
    private UserDTO user;
    private Long totalLikes;

    public Posts convertPostDTOToPost() {
        Posts post = new Posts();
        post.setId(this.getId());
        post.setDate(this.getDate());
        post.setContent(this.getContent());
        post.setActive(this.getActive());
        post.setIsEdited(this.getIsEdited());

        if (this.getUser() != null) {
            post.setUser(this.getUser().convertUserDTOToUser());
        }

        return post;
    }
}