package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.enums.LikeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostResponseDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime date;
    private String content;
    private UserPostResponseDTO user;
    private Long totalLikes;
    private Long totalComments;
    private LikeEnum userLike;
    private Boolean postOwner;
    private Boolean isEdited;

    public PostResponseDTO(Object[] row) {
        this.id = (Long) row[0];
        this.date = ((java.sql.Timestamp) row[1]).toLocalDateTime();
        this.content = (String) row[2];
        this.user = new UserPostResponseDTO((Long) row[3], (String) row[4]);
        this.totalLikes = (Long) row[5];
        this.totalComments = (Long) row[6];
        this.userLike = (row[7] != null) ? LikeEnum.valueOf((String) row[7]) : null;
        this.postOwner = (Boolean) row[8];
        this.isEdited = (Boolean) row[9];
    }
}