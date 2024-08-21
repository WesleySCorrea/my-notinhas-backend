package my.notinhas.project.repositories;

import my.notinhas.project.entities.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

public interface CommunityRepository extends JpaRepository<Community, Long> {

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
        c.created
    FROM 
        Community c
    LEFT JOIN 
        c.owner u
    LEFT JOIN 
        c.members m
    LEFT JOIN 
        c.posts p
    GROUP BY 
        c.id, c.name, c.description, u.id, u.googleId, u.userName, c.created
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
        c.created
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
    GROUP BY 
        c.id, c.name, c.description, o.id, o.user_name, c.created
    """, nativeQuery = true)
    Page<Object[]> findAllCommunityByUser(@Param("userId") Long userId, Pageable pageable);
}
