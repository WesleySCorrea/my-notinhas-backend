package my.notinhas.project.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Users;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long id;
    private String userName;
    private String bio;
    private LocalDateTime created;

    public UserProfileDTO converterUserToUserProfile(Users user) {

        UserProfileDTO userProfile = new UserProfileDTO();

        userProfile.setId(user.getId());
        userProfile.setUserName(user.getUserName());
        userProfile.setBio(Optional.ofNullable(user.getBio()).orElse(""));
        userProfile.setCreated(user.getCreated());

        return userProfile;
    }
}
