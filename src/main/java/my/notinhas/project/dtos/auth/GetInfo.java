package my.notinhas.project.dtos.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetInfo {

    @JsonProperty("id")
    private String googleId;
    private String email;
    @JsonProperty("verified_email")
    private boolean verifiedEmail;
    private String name;
    @JsonProperty("given_name")
    private String firstName;
    @JsonProperty("family_name")
    private String lastName;
    private String locale;
    private String picture;
}
