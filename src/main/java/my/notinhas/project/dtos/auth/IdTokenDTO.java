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
public class IdTokenDTO {
    @JsonProperty("access_token")
    public String accessToken;
    public String refresh_token;
    public int expires_in;
    public String scope;
    public String token_type;
    public String id_token;
}
