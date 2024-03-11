package my.notinhas.project.dtos.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.UserDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginDTO {
    public IdTokenDTO token;

    public UserDTO user;
}
