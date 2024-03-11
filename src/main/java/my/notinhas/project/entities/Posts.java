package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    private LocalDate date;
    @Column(name = "content")
    private String content;
    @Column(name = "active")
    private Boolean active;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likes> likes;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
