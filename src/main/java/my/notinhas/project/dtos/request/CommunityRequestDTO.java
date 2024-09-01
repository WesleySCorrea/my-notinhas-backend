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
public class CommunityRequestDTO {

    private String title;
    private String description;
    private Boolean protectedCommunity;

    public Community toEntity(UserDTO userDTO) {
        var community = new Community();
        community.setName(this.title);
        community.setDescription(this.description);
        community.setOwner(userDTO.convertUserDTOToUser());
        community.setCreated(LocalDateTime.now());
        community.setProtectedCommunity(this.protectedCommunity);
        return community;
    }

}