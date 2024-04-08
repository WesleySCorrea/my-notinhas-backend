package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.enums.LikeEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LikeToUserDTO {
    private Long id;
    private LikeEnum likeEnum;
    private String postContent;

    public LikeToUserDTO converterLikeToLikeToUserDTO(Likes likes) {
        LikeToUserDTO likeDTO = new LikeToUserDTO();
        likeDTO.setId(likes.getId());
        likeDTO.setLikeEnum(likes.getLikeEnum());
        likeDTO.setPostContent(likes.getPost().getContent());
        return likeDTO;
    }
}