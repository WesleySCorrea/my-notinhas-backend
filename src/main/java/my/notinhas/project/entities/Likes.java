package my.notinhas.project.entities;

import jakarta.persistence.*;
import my.notinhas.project.enums.Value;

@Entity
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "value")
    @Enumerated(value = EnumType.STRING)
    private Value value;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;
}
