package my.notinhas.project.repositories;

import my.notinhas.project.entities.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

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

}
