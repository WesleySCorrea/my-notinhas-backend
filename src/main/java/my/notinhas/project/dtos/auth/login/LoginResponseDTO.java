package my.notinhas.project.dtos.auth.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginResponseDTO {

    private String idToken;
    private String userName;
    private String picture;

    public LoginResponseDTO(String idToken) {
        this.idToken = idToken;
    }
}
