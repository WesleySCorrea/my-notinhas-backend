package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.enums.ReactionEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserHistoryResponseDTO {
    private ReactionEnum reaction;
    private String content;
    private Long postId;
    private Long commentId;
    private Long parentCommentId;
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime date;

    public UserHistoryResponseDTO(Object[] history) {

        this.reaction = ReactionEnum.valueOf((String) history[0]);
        this.content = (String) history[1];
        this.postId = (Long) history[2];
        this.commentId = (Long) history[3];
        this.parentCommentId = (Long) history[4];
        this.date = ((java.sql.Timestamp) history[5]).toLocalDateTime();
        adjustContent();
    }
    public void adjustContent() {
        if (this.content != null && this.content.length() > 30) {
            this.content = this.content.substring(0, 27) + "...";
        }
    }
}