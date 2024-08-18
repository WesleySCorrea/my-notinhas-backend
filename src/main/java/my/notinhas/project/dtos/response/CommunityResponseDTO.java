package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommunityResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Users owner;
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime created;

    public CommunityResponseDTO(Community community) {
        this.id = community.getId();
        this.title = community.getName();
        this.description = community.getDescription();
        this.created = community.getCreated();
    }
}