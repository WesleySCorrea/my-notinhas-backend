package my.notinhas.project.repositories;

import my.notinhas.project.entities.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findAllByActiveTrue(Pageable pageable);
}
