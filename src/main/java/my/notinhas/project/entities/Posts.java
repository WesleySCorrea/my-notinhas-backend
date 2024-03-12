package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.enums.Value;

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
    @Column(name = "total_likes")
    private Long totalLikes;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likes> likes;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public void calculateTotalLikesAndDeslikes(List<Likes> likes) {
        this.totalLikes = 0L;

        for (Likes like : likes) {
            if (like.getValue() == Value.LIKE) {
                this.totalLikes++;
            } else if (like.getValue() == Value.DESLIKE) {
                this.totalLikes--;
            }
        }
    }
}
