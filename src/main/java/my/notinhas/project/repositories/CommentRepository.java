package my.notinhas.project.repositories;

import my.notinhas.project.entities.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    Long countByPostIdAndActiveIsTrue(Long postId);
    Page<Comments> findAllByParentCommentIdAndActiveIsTrue(Pageable pageable, Long id);
    Page<Comments> findByUserIdAndActiveIsTrue(Long userId, Pageable pageable);
    List<Comments> findByUserIdAndPostActiveIsTrueOrderByDateDesc(Long userId);
    List<Comments> findByPostIdAndParentCommentIsNullAndActiveIsTrue(Long postId);
    Page<Comments> findByPostIdAndParentCommentIsNullAndActiveIsTrue(Long userId, Pageable pageable);
}
