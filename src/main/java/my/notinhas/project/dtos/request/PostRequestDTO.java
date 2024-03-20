package my.notinhas.project.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Users;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostRequestDTO {
    private String content;
    private UserDTO user;
}