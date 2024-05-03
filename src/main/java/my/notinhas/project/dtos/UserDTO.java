package my.notinhas.project.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Users;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {
    private Long id;
    private String googleId;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
    private LocalDateTime created;
    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
    private LocalDateTime editatedUsername;
    private String bio;
    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")

    private LocalDateTime editatedBio;
    private Boolean active;

    public UserDTO (Users user) {

        this.id = user.getId();
        this.googleId = user.getGoogleId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.picture = user.getPicture();
        this.created = user.getCreated();
        this.editatedUsername = user.getEditatedUsername();
        this.bio = user.getBio();
        this.editatedBio = user.getEditatedBio();
        this.active = user.getActive();
    }

    public Users convertUserDTOToUser() {

        return new Users(
                this.id,
                this.googleId,
                this.userName,
                this.email,
                this.firstName,
                this.lastName,
                this.picture,
                this.created,
                this.editatedUsername,
                this.bio,
                this.editatedBio,
                this.active
        );
    }

    public UserDTO createUserDTOWithUserName (GoogleIdToken googleIdToken) {

        String googleId = googleIdToken.getPayload().get("sub").toString();

        char number1 = googleId.charAt(3);
        char number2 = googleId.charAt(9);
        char number3 = googleId.charAt(11);
        char number4 = googleId.charAt(15);
        char number5 = googleId.charAt(20);

        String userName = googleIdToken.getPayload().get("given_name").toString() + number1 + number2 + number3 + number4 + number5;

        String familyName = null;
        if (googleIdToken.getPayload().containsKey("family_name")) {
            familyName = googleIdToken.getPayload().get("family_name").toString();
        }

        return new UserDTO(
                null,
                googleId,
                userName,
                googleIdToken.getPayload().getEmail(),
                googleIdToken.getPayload().get("given_name").toString(),
                familyName,
                googleIdToken.getPayload().get("picture").toString(),
                LocalDateTime.now(),
                null,
                null,
                null,
                true
        );
    }
}