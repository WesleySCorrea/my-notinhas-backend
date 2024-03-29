package my.notinhas.project.repositories;

import my.notinhas.project.entities.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    Long countByPostId(Long postId);
    List<Comments> findByPostIdAndParentCommentIsNull(Long postId);
    Page<Comments> findByPostIdAndParentCommentIsNull(Long postId, Pageable pageable);
}
