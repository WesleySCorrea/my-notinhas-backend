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
    @Query(value = "SELECT id, date, content FROM public.posts p WHERE p.active = TRUE AND p.community_id IS NULL AND p.target_user_id IS NULL ORDER BY p.date DESC", nativeQuery = true)
    Page<Object[]> findAllPostsByPublic(Pageable pageable);

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
    WHERE p.active = TRUE AND p.community_id IS NULL AND p.target_user_id IS NULL
    ORDER BY p.date DESC
    """, nativeQuery = true)
    Page<Object[]> findActivePosts(@Param("userId") Long userId, Pageable pageable);

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
    WHERE p.active = TRUE AND p.community_id = :communityId
    ORDER BY p.date DESC
    """, nativeQuery = true)
    Page<Object[]> findActivePostsCommunityId(@Param("userId") Long userId, @Param("communityId") Long communityId, Pageable pageable);




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
    WHERE p.active = TRUE  AND p.content ILIKE %:content%
    ORDER BY p.date DESC
    """, nativeQuery = true)
    Page<Object[]> findActivePostsContaining(@Param("userId") Long userId, @Param("content") String content, Pageable pageable);

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
    WHERE p.active = TRUE AND p.target_user_id = :targetUserId
    ORDER BY p.date DESC
    """, nativeQuery = true)
    Page<Object[]> findActivePostsTargetUserId(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId, Pageable pageable);
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
    WHERE p.active = TRUE AND p.id = :postId
    ORDER BY p.date DESC
    """, nativeQuery = true)
    List<Object[]> findActivePostById(@Param("userId") Long userId, @Param("postId") Long postId);
    Page<Posts> findByUserIdAndActiveIsTrueOrderByDateDesc(Long userId, Pageable pageable);
    List<Posts> findByUserUserNameAndActiveIsTrue(String username);
    @Query(value = """
        SELECT * FROM (
            SELECT 
                'POST' AS reaction, 
                p.content, 
                p.id AS post_id, 
                NULL AS comment_id, 
                NULL AS parent_comment_id, 
                p.date 
            FROM 
                posts p 
            WHERE 
                p.user_id = :userId AND p.active = TRUE 
            UNION ALL 
            SELECT 
                'COMMENT' AS reaction, 
                c.content, 
                c.post_id AS post_id, 
                c.id AS comment_id, 
                c.parent_comment_id AS parent_comment_id, 
                c.date 
            FROM 
                comments c 
            WHERE 
                c.user_id = :userId AND EXISTS ( 
                    SELECT 1 
                    FROM posts p 
                    WHERE p.id = c.post_id AND p.active = TRUE 
                ) 
            UNION ALL 
            SELECT 
                CASE 
                    WHEN l.like_enum = 'LIKE' THEN 'LIKE' 
                    ELSE 'DISLIKE' 
                END AS reaction, 
                p.content, 
                p.id AS post_id, 
                NULL AS comment_id, 
                NULL AS parent_comment_id, 
                l.date 
            FROM 
                likes l 
            JOIN 
                posts p ON l.post_id = p.id 
            WHERE 
                l.user_id = :userId 
            UNION ALL 
            SELECT 
                CASE 
                    WHEN lc.like_enum = 'LIKE' THEN 'LIKE_COM' 
                    ELSE 'DISLIKE_COM' 
                END AS reaction, 
                c.content, 
                p.id AS post_id, 
                lc.comment_id AS comment_id, 
                c.parent_comment_id AS parent_comment_id, 
                lc.date 
            FROM 
                likes_comments lc 
            JOIN 
                comments c ON lc.comment_id = c.id 
            JOIN 
                posts p ON c.post_id = p.id 
            WHERE 
                lc.user_id = :userId 
        ) AS history 
        ORDER BY date DESC
        """,
            countQuery = """
            SELECT COUNT(*) FROM (
                SELECT 
                    'POST' 
                FROM 
                    posts p 
                WHERE 
                    p.user_id = :userId AND p.active = TRUE 
                UNION ALL 
                SELECT 
                    'COMMENT' 
                FROM 
                    comments c 
                WHERE 
                    c.user_id = :userId AND EXISTS ( 
                        SELECT 1 
                        FROM posts p 
                        WHERE p.id = c.post_id AND p.active = TRUE 
                    ) 
                UNION ALL 
                SELECT 
                    CASE 
                        WHEN l.like_enum = 'LIKE' THEN 'LIKE' 
                        ELSE 'DISLIKE' 
                    END 
                FROM 
                    likes l 
                JOIN 
                    posts p ON l.post_id = p.id 
                WHERE 
                    l.user_id = :userId 
                UNION ALL 
                SELECT 
                    CASE 
                        WHEN lc.like_enum = 'LIKE' THEN 'LIKE_COM' 
                        ELSE 'DISLIKE_COM' 
                    END 
                FROM 
                    likes_comments lc 
                JOIN 
                    comments c ON lc.comment_id = c.id 
                JOIN 
                    posts p ON c.post_id = p.id 
                WHERE 
                    lc.user_id = :userId 
            ) AS count_history
        """,
            nativeQuery = true)
    Page<Object[]> findUserHistoryByUserId(@Param("userId") Long userId, Pageable pageable);


}
