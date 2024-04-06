package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Users;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserIDResponseDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String picture;
    private List<LikeToUserDTO> likeReactions;
    private List<CommentToUserDTO> commentsOwner;

    public UserIDResponseDTO converterUserToUserIDResponse(Users user) {

        UserIDResponseDTO userResponse = new UserIDResponseDTO();
        userResponse.setUserName(user.getUserName());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPicture(user.getPicture());

        return userResponse;
    }
}