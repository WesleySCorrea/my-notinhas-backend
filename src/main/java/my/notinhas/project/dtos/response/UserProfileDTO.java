package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Long userId;
    private String userName;
    private String bio;
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime created;

    public UserProfileDTO converterUserToUserProfile(Users user) {

        UserProfileDTO userProfile = new UserProfileDTO();

        userProfile.setUserId(user.getId());
        userProfile.setUserName(user.getUserName());
        userProfile.setBio(Optional.ofNullable(user.getBio()).orElse(""));
        userProfile.setCreated(user.getCreated());

        return userProfile;
    }
}
