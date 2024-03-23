package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import my.notinhas.project.enums.LikeEnum;

@Entity
@Getter
@Setter
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "like_enum")
    @Enumerated(value = EnumType.STRING)
    private LikeEnum likeEnum;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
