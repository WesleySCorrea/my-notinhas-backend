package my.notinhas.project.repositories;

import my.notinhas.project.entities.Community;
import my.notinhas.project.entities.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
}
