package my.notinhas.project.mapper;

import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Community;
import my.notinhas.project.entities.CommunityMember;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.StatusMemberEnum;
import my.notinhas.project.services.ExtractUser;

import java.time.LocalDateTime;

public class CommunityMemberMapper {

    public static CommunityMember createCommunityMember(Long communityId, Long userId, StatusMemberEnum statusMember) {

        Community community = new Community();
        community.setId(communityId);

        Users user = new Users();
        user.setId(userId);

        CommunityMember communityMember = new CommunityMember();
        communityMember.setCommunity(community);
        communityMember.setUser(user);
        communityMember.setJoinedDate(LocalDateTime.now());
        communityMember.setStatusMember(statusMember);

        return communityMember;
    }
}
