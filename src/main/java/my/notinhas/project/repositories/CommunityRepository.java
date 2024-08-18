package my.notinhas.project.repositories;

import my.notinhas.project.entities.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
