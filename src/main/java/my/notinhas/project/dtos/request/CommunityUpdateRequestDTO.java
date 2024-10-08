package my.notinhas.project.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Community;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityUpdateRequestDTO {
    private String description;

    public Community toEntity(UserDTO userDTO) {
        var community = new Community();
        community.setDescription(this.description);
        community.setOwner(userDTO.convertUserDTOToUser());
        community.setCreated(LocalDateTime.now());
        return community;
    }
}