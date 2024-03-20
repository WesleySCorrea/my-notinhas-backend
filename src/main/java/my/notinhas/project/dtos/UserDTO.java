package my.notinhas.project.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    public UserDTO createUserDTOWithUserName (GoogleIdToken googleIdToken) {

        String googleId = googleIdToken.getPayload().get("sub").toString();

        char number1 = googleId.charAt(3);
        char number2 = googleId.charAt(9);
        char number3 = googleId.charAt(11);
        char number4 = googleId.charAt(15);
        char number5 = googleId.charAt(20);

        String userName = googleIdToken.getPayload().get("given_name").toString() + number1 + number2 + number3 + number4 + number5;

        UserDTO userDTO = new UserDTO(
                null,
                googleId,
                userName,
                googleIdToken.getPayload().getEmail(),
                googleIdToken.getPayload().get("given_name").toString(),
                googleIdToken.getPayload().get("family_name").toString(),
                googleIdToken.getPayload().get("picture").toString()
        );

        return userDTO;
    }
}