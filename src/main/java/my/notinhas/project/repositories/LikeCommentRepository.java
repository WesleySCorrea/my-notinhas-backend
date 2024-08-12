package my.notinhas.project.repositories;

import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.enums.LikeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeCommentRepository extends JpaRepository<LikesComments, Long> {
    LikesComments findByUserIdAndCommentId(Long userId, Long commentId);
    List<LikesComments> findByUserUserName(String userName);
}
