package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import my.notinhas.project.enums.LikeEnum;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "likes_comments", uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "user_id"}))
public class LikesComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "like_enum")
    @Enumerated(value = EnumType.STRING)
    private LikeEnum likeEnum;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comments comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
