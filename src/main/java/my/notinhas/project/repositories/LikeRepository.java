package my.notinhas.project.repositories;

import my.notinhas.project.entities.Likes;
import my.notinhas.project.enums.LikeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Long countByPostIdAndLikeEnum(Long postId, LikeEnum likeEnum);
    Likes findByUserIdAndPostId(Long userId, Long postId);
    List<Likes> findByUserId(Long userId);
}
