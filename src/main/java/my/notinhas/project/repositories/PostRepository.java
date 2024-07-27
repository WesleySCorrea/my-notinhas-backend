package my.notinhas.project.repositories;

import jakarta.transaction.Transactional;
import my.notinhas.project.entities.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findAllByActiveTrueOrderByDateDesc(Pageable pageable);
    @Query(value = "SELECT id, date, content from public.posts p WHERE p.active = TRUE ORDER BY p.date DESC", nativeQuery = true)
    Page<Object[]> findAllPostsByPublic(Pageable pageable);
    List<Posts> findByUserIdAndActiveIsTrueOrderByDateDesc(Long userId);
    List<Posts> findByUserUserNameAndActiveIsTrue(String username);

    @Query("SELECT p FROM Posts p WHERE p.content ILIKE %:content% AND p.active = true ORDER BY p.date DESC")
    Page<Posts> findByContentContainingCaseSensitiveAndActiveTrue(@Param("content") String content, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Posts SET active = false WHERE id = :id AND active = true", nativeQuery = true)
    int updateActiveFalse(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Posts p SET p.content = :content, p.date = :date, p.isEdited = :isEdited WHERE p.id = :id AND p.active = true")
    int updatePostContentAndDate(@Param("id") Long id, @Param("content") String content, @Param("date") LocalDateTime date, @Param("isEdited") Boolean isEdited);

    @Query(value = """
    SELECT p.id, p.date, p.content, p.user_id AS userId, u.user_name AS userName,
           COALESCE(like_counts.total_likes, 0) - COALESCE(like_counts.total_dislikes, 0) AS totalLikes,
           COALESCE(reply_counts.total_comments, 0) AS totalComments,
           user_like_status.like_enum AS userLike,
           CASE WHEN p.user_id = :userId THEN TRUE ELSE FALSE END AS postOwner,
           p.is_edited AS isEdited
    FROM public.posts p
    LEFT JOIN public.users u ON p.user_id = u.id
    LEFT JOIN (
        SELECT lc.post_id, 
               SUM(CASE WHEN lc.like_enum = 'LIKE' THEN 1 ELSE 0 END) AS total_likes,
               SUM(CASE WHEN lc.like_enum = 'DISLIKE' THEN 1 ELSE 0 END) AS total_dislikes
        FROM likes lc
        GROUP BY lc.post_id
    ) like_counts ON p.id = like_counts.post_id
    LEFT JOIN (
        SELECT c.post_id, COUNT(*) AS total_comments
        FROM comments c
        WHERE c.active IS TRUE
        GROUP BY c.post_id
    ) reply_counts ON p.id = reply_counts.post_id
    LEFT JOIN (
        SELECT lc.post_id, lc.like_enum
        FROM likes lc
        WHERE lc.user_id = :userId
    ) user_like_status ON p.id = user_like_status.post_id
    WHERE p.active = TRUE
    ORDER BY p.date DESC
    """, nativeQuery = true)
    Page<Object[]> findActivePosts(@Param("userId") Long userId, Pageable pageable);
}
