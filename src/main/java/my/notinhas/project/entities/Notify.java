package my.notinhas.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import my.notinhas.project.enums.ActionEnum;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "notify")
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "notify_owner")
    private String NotifyOwner;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comments comment;
    @NotNull
    @Column(name = "action_enum")
    @Enumerated(value = EnumType.STRING)
    private ActionEnum actionEnum;
    @Column(name = "verified")
    private Boolean verified = Boolean.FALSE;
    @Column(name = "date")
    private LocalDateTime date;
}
