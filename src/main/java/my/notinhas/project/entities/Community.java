package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "communities")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    @Column(name = "created")
    private LocalDateTime created;

    @ManyToMany
    @JoinTable(
            name = "community_members",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> members;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Posts> posts;

    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active;
}
