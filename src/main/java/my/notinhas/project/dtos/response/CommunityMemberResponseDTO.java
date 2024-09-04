package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.CommunityMember;
import my.notinhas.project.enums.RoleEnum;
import my.notinhas.project.enums.StatusMemberEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommunityMemberResponseDTO {

    private Long id;
    private Long userId;
    private StatusMemberEnum statusMemberEnum;
    private RoleEnum rolesEnum;

    public CommunityMemberResponseDTO(CommunityMember communityMember) {
        this.id = communityMember.getId();
        this.userId = communityMember.getUser().getId();
        this.statusMemberEnum = communityMember.getStatusMember();
        this.rolesEnum = communityMember.getRoleMember();
    }
}
