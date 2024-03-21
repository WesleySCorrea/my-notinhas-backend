package my.notinhas.project.repositories;

import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.enums.Value;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Long countByPostIdAndValue(Long postId, Value value);
}
