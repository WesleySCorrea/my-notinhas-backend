package my.notinhas.project.repositories;

import my.notinhas.project.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    Long countByPostId(Long postId);
}
