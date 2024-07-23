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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notify_owner_id")
    private Users notifyOwner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comments comment;
    @Column(name = "parent_id")
    private Long parentId;
    @NotNull
    @Column(name = "action_enum")
    @Enumerated(value = EnumType.STRING)
    private ActionEnum actionEnum;
    @Column(name = "verified")
    private Boolean verified = Boolean.FALSE;
    @Column(name = "date")
    private LocalDateTime date;
}
