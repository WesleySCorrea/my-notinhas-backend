package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Community;
import org.springframework.data.domain.Page;

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
    private Long ownerId;
    private String owmerUserName;
    private Long totalMembers;
    private Long totalPosts;
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime created;

    public CommunityResponseDTO(Community community) {
        this.id = community.getId();
        this.title = community.getName();
        this.description = community.getDescription();
        this.created = community.getCreated();
    }

    public CommunityResponseDTO(Object[] row) {

        this.id = (Long) row[0];
        this.title = (String) row[1];
        this.description = (String) row[2];
        this.ownerId = (Long) row[3];
        this.owmerUserName = (String) row[4];
        this.totalMembers = (Long) row[5];
        this.totalPosts = (Long) row[6];
        this.created = ((java.sql.Timestamp) row[7]).toLocalDateTime();
    }

    public Page<CommunityResponseDTO> convertToCommunityResponseDTO(Page<Object[]> page) {
        return page.map(objects -> {
            Long id = (Long) objects[0];
            String title = (String) objects[1];
            String description = (String) objects[2];
            Long ownerId = (Long) objects[3];
            String googleId = (String) objects[4];
            String ownerUserName = (String) objects[5];
            Long totalMembers = (Long) objects[6];
            Long totalPosts = (Long) objects[7];
            LocalDateTime created = (LocalDateTime) objects[8];

            return new CommunityResponseDTO(id, title, description, ownerId, ownerUserName, totalMembers, totalPosts, created);
        });
    }
}