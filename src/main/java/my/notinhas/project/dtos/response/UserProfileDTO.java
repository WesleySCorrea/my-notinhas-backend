package my.notinhas.project.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Users;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private String userName;
    private String bio;

    public UserProfileDTO converterUserToUserProfile(Users user) {

        UserProfileDTO userProfile = new UserProfileDTO();
        userProfile.setUserName(user.getUserName());
        userProfile.bio = user.getBio();

        return userProfile;
    }
}
