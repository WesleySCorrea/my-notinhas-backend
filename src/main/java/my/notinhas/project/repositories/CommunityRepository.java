package my.notinhas.project.repositories;

import jakarta.transaction.Transactional;
import my.notinhas.project.entities.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query(value = """
    SELECT
        c.id AS community_id,
        c.name AS community_name,
        c.description AS community_description,
        o.id AS owner_id,
        o.user_name AS owner_username,
        COUNT(DISTINCT m.user_id) AS total_members,
        COUNT(DISTINCT p.id) AS total_posts,
        c.created,
        c.protected_community
    FROM
        public.communities c
    LEFT JOIN
        public.community_members m ON c.id = m.community_id
    LEFT JOIN
        public.posts p ON c.id = p.community_id
    INNER JOIN
        public.users u ON m.user_id = u.id
    INNER JOIN
        public.users o ON c.owner_id = o.id
    WHERE
        c.id = :communityId
        and c.active = true
    GROUP BY
        c.id, c.name, c.description, o.id, o.user_name, c.created, c.protected_community
    """, nativeQuery = true)
    List<Object[]> findByIdAndActiveTrue(@Param("communityId") Long communityId);

    @Query("""
    SELECT
        c.id,
        c.name AS title,
        c.description,
        u.id AS userId,
        u.googleId,
        u.userName,
        COUNT(DISTINCT m.id) AS totalMembers,
        COUNT(DISTINCT p.id) AS totalPosts,
        c.created,
        c.protectedCommunity
    FROM
        Community c
    LEFT JOIN
        c.owner u
    LEFT JOIN
        c.members m
    LEFT JOIN
        c.posts p
   WHERE
        c.active = true
    GROUP BY
        c.id, c.name, c.description, u.id, u.googleId, u.userName, c.created, c.protectedCommunity
    """)
    Page<Object[]> findAllPCommunities(Pageable pageable);
    @Query(value = """
    SELECT
        c.id AS community_id,
        c.name AS community_name,
        c.description AS community_description,
        o.id AS owner_id,
        o.user_name AS owner_username,
        COUNT(DISTINCT m.user_id) AS total_members,
        COUNT(DISTINCT p.id) AS total_posts,
        c.created,
        c.protected_community
    FROM
        public.communities c
    LEFT JOIN
        public.community_members m ON c.id = m.community_id
    LEFT JOIN
        public.posts p ON c.id = p.community_id
    INNER JOIN
        public.users u ON m.user_id = u.id
    INNER JOIN
        public.users o ON c.owner_id = o.id
    WHERE
        u.id = :userId
        and c.active = true
    GROUP BY
        c.id, c.name, c.description, o.id, o.user_name, c.created, c.protected_community
    """, nativeQuery = true)
    Page<Object[]> findAllCommunityByUser(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
    SELECT
        c.id AS community_id,
        c.name AS community_name,
        c.description AS community_description,
        o.id AS owner_id,
        o.user_name AS owner_username,
        COUNT(DISTINCT m.user_id) AS total_members,
        COUNT(DISTINCT p.id) AS total_posts,
        c.created,
        c.protected_community
    FROM
        public.communities c
    LEFT JOIN
        public.community_members m ON c.id = m.community_id
    LEFT JOIN
        public.posts p ON c.id = p.community_id
    INNER JOIN
        public.users u ON m.user_id = u.id
    INNER JOIN
        public.users o ON c.owner_id = o.id
    WHERE
        c.owner_id = :userId
        and c.active = true
    GROUP BY
        c.id, c.name, c.description, o.id, o.user_name, c.created, c.protected_community
    """, nativeQuery = true)
    Page<Object[]> findAllCommunityByOwner(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Community c SET c.description = :description WHERE c.id = :id AND c.active = true")
    int updateCommunityDescription(@Param("id") Long id, @Param("description") String description);
}
