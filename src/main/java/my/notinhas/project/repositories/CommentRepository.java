package my.notinhas.project.repositories;

import my.notinhas.project.entities.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    Page<Comments> findByUserIdAndActiveIsTrue(Long userId, Pageable pageable);
    List<Comments> findByUserUserNameAndActiveIsTrue(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE comments SET active = false WHERE parent_comment_id = :parentId", nativeQuery = true)
    void deactivateCommentsByParentId(Long parentId);

    @Query(value = "SELECT " +
            "    c.id, " +
            "    c.date, " +
            "    c.content, " +
            "    c.is_edited, " +
            "    COALESCE(like_counts.total_likes, 0) AS total_likes, " +
            "    COALESCE(reply_counts.total_replies, 0) AS total_replies, " +
            "    user_like_status.like_enum AS UserLike, " +
            "    CASE WHEN c.user_id = :userId THEN TRUE ELSE FALSE END AS comment_owner, " +
            "    c.user_id, " +
            "    u.user_name " +
            "FROM " +
            "    comments c " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        lc.comment_id, " +
            "        SUM(CASE WHEN lc.like_enum = 'LIKE' THEN 1 ELSE 0 END) - " +
            "        SUM(CASE WHEN lc.like_enum = 'DISLIKE' THEN 1 ELSE 0 END) AS total_likes " +
            "    FROM " +
            "        likes_comments lc " +
            "    GROUP BY " +
            "        lc.comment_id " +
            ") like_counts ON c.id = like_counts.comment_id " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        rc.parent_comment_id, " +
            "        COUNT(*) AS total_replies " +
            "    FROM " +
            "        comments rc " +
            "    WHERE " +
            "        rc.active = TRUE " +
            "    GROUP BY " +
            "        rc.parent_comment_id " +
            ") reply_counts ON c.id = reply_counts.parent_comment_id " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        lc.comment_id, " +
            "        lc.like_enum " +
            "    FROM " +
            "        likes_comments lc " +
            "    WHERE " +
            "        lc.user_id = :userId " +
            ") user_like_status ON c.id = user_like_status.comment_id " +
            "LEFT JOIN users u ON c.user_id = u.id " +
            "WHERE " +
            "    c.post_id = :postId " +
            "    AND c.parent_comment_id IS NULL " +
            "    AND c.active = TRUE",
            nativeQuery = true)
    Page<Object[]> findByPostIdAndParentCommentIsNull(@Param("postId") Long postId, @Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT " +
            "    c.id, " +
            "    c.date, " +
            "    c.content, " +
            "    c.is_edited, " +
            "    COALESCE(like_counts.total_likes, 0) AS total_likes, " +
            "    COALESCE(reply_counts.total_replies, 0) AS total_replies, " +
            "    user_like_status.like_enum AS UserLike, " +
            "    CASE WHEN c.user_id = :userId THEN TRUE ELSE FALSE END AS comment_owner, " +
            "    c.user_id, " +
            "    u.user_name " +
            "FROM " +
            "    comments c " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        lc.comment_id, " +
            "        SUM(CASE WHEN lc.like_enum = 'LIKE' THEN 1 ELSE 0 END) - " +
            "        SUM(CASE WHEN lc.like_enum = 'DISLIKE' THEN 1 ELSE 0 END) AS total_likes " +
            "    FROM " +
            "        likes_comments lc " +
            "    GROUP BY " +
            "        lc.comment_id " +
            ") like_counts ON c.id = like_counts.comment_id " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        rc.parent_comment_id, " +
            "        COUNT(*) AS total_replies " +
            "    FROM " +
            "        comments rc " +
            "    GROUP BY " +
            "        rc.parent_comment_id " +
            ") reply_counts ON c.id = reply_counts.parent_comment_id " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        lc.comment_id, " +
            "        lc.like_enum " +
            "    FROM " +
            "        likes_comments lc " +
            "    WHERE " +
            "        lc.user_id = :userId " +
            ") user_like_status ON c.id = user_like_status.comment_id " +
            "LEFT JOIN users u ON c.user_id = u.id " +
            "WHERE " +
            "    c.parent_comment_id = :parentCommentId " +
            "    AND c.active = TRUE",
            nativeQuery = true)
    Page<Object[]> findByParentCommentId(@Param("parentCommentId") Long postId, @Param("userId") Long userId, Pageable pageable);

    @Query(value = """
        SELECT 
            c.id, 
            c.date, 
            c.content, 
            c.is_edited, 
            COALESCE(like_counts.total_likes, 0) AS total_likes, 
            COALESCE(reply_counts.total_replies, 0) AS total_replies,
            user_like_status.like_enum AS user_like,
            CASE WHEN c.user_id = :userId THEN TRUE ELSE FALSE END AS comment_owner,
            c.user_id,
            u.user_name
        FROM 
            public.comments c
        LEFT JOIN (
            SELECT 
                lc.comment_id, 
                SUM(CASE WHEN lc.like_enum = 'LIKE' THEN 1 ELSE 0 END) - 
                SUM(CASE WHEN lc.like_enum = 'DISLIKE' THEN 1 ELSE 0 END) AS total_likes
            FROM 
                likes_comments lc
            GROUP BY 
                lc.comment_id
        ) like_counts ON c.id = like_counts.comment_id
        LEFT JOIN (
            SELECT 
                rc.parent_comment_id, 
                COUNT(*) AS total_replies
            FROM 
                comments rc
            WHERE 
                rc.active = TRUE
            GROUP BY 
                rc.parent_comment_id
        ) reply_counts ON c.id = reply_counts.parent_comment_id
        LEFT JOIN (
            SELECT 
                lc.comment_id, 
                lc.like_enum
            FROM 
                likes_comments lc
            WHERE 
                lc.user_id = :userId
        ) user_like_status ON c.id = user_like_status.comment_id
        LEFT JOIN users u ON c.user_id = u.id
        WHERE 
            c.id = :commentId
        """, nativeQuery = true)
    List<Object[]> findCommentByIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);
}
