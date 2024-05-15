package my.notinhas.project.repositories;

import my.notinhas.project.entities.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findAllByActiveTrueOrderByDateDesc(Pageable pageable);
    List<Posts> findByUserIdAndActiveIsTrueOrderByDateDesc(Long userId);
    List<Posts> findByUserUserNameAndActiveIsTrue(String username);

    @Query("SELECT p FROM Posts p WHERE p.content ILIKE %:content% AND p.active = true ORDER BY p.date DESC")
    Page<Posts> findByContentContainingCaseSensitiveAndActiveTrue(@Param("content") String content, Pageable pageable);
}
