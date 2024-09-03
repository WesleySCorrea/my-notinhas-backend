package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.notinhas.project.enums.RoleEnum;
import my.notinhas.project.enums.StatusMemberEnum;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "community_members")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommunityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    @Column(name = "joined_date", nullable = false)
    private LocalDateTime joinedDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_member", nullable = false)
    private RoleEnum roleMember = RoleEnum.MEMBER;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_member", nullable = false)
    private StatusMemberEnum statusMember = StatusMemberEnum.NOT_MEMBER;
}
