package my.notinhas.project.repositories;

import my.notinhas.project.entities.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    Long countByPostIdAndActiveIsTrue(Long postId);
    Page<Comments> findAllByParentCommentIdAndActiveIsTrue(Pageable pageable, Long id);
    Page<Comments> findByUserIdAndActiveIsTrue(Long userId, Pageable pageable);
    List<Comments> findByUserIdAndPostActiveIsTrueOrderByDateDesc(Long userId);
    List<Comments> findByPostIdAndParentCommentIsNullAndActiveIsTrue(Long postId);
    Page<Comments> findByPostIdAndParentCommentIsNullAndActiveIsTrue(Long userId, Pageable pageable);
    List<Comments> findByUserUserNameAndActiveIsTrue(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE comments SET active = false WHERE parent_comment_id = :parentId", nativeQuery = true)
    void deactivateCommentsByParentId(Long parentId);
}
