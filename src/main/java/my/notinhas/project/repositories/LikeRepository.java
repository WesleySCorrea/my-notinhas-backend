package my.notinhas.project.repositories;

import my.notinhas.project.entities.Likes;
import my.notinhas.project.enums.LikeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByUserIdAndPostId(Long userId, Long postId);
    Page<Likes> findByUserId(Long userId, Pageable pageable);
    List<Likes> findByUserUserName(String userName);
}
