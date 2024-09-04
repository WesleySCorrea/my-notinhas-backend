package my.notinhas.project.repositories;

import jakarta.transaction.Transactional;
import my.notinhas.project.entities.CommunityMember;
import my.notinhas.project.enums.StatusMemberEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

    @Transactional
    void deleteByCommunityIdAndUserIdAndStatusMember(Long communityId, Long userId, StatusMemberEnum statusMember);

    @Modifying
    @Transactional
    @Query(value = "UPDATE community_members SET status_member = 'JOINED' WHERE community_id = ?1 AND user_id = ?2", nativeQuery = true)
    void updateStatusMemberByCommunityIdAndUserId(Long communityId, Long userId);

    Page<CommunityMember> findByCommunityId(Long communityId, Pageable pageable);
}
