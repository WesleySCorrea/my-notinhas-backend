package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "posts")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Posts {

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "content")
    private String content;
    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active;
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private List<Likes> likes;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private Users user;
}
