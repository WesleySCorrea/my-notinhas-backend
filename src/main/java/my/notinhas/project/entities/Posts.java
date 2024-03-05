package my.notinhas.project.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "posts")
public class Posts {

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name")
    private String userName;
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
