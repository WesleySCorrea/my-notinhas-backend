package my.notinhas.project.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Community;
import my.notinhas.project.entities.Users;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRequestDTO {

    private String title;
    private String description;

    public Community toEntity(Long userId) {
        var community = new Community();
        community.setName(this.title);
        community.setDescription(this.description);
        community.setOwner(new Users(userId));
        community.setCreated(LocalDateTime.now());
        return community;
    }

}