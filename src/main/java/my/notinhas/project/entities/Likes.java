package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.notinhas.project.enums.Value;

@Entity
@Getter
@Setter
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "value")
    @Enumerated(value = EnumType.STRING)
    private Value value;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;
}
