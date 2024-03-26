package my.notinhas.project.repositories;

import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.enums.LikeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikesComments, Long> {
    Long countByCommentIdAndLikeEnum(Long postId, LikeEnum likeEnum);
    LikesComments findByUserIdAndCommentId(Long userId, Long commentId);
}
