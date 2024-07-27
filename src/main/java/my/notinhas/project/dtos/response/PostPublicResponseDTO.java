package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Posts;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostPublicResponseDTO {
    private Long id;
    private String content;
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime date;

    public PostPublicResponseDTO(Posts post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.date = post.getDate();
    }

    public PostPublicResponseDTO(Object[] row) {
        this.id = (Long) row[0];
        this.date = ((java.sql.Timestamp) row[1]).toLocalDateTime();
        this.content = (String) row[2];
    }
}