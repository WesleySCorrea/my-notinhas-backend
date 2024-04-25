package my.notinhas.project.repositories;

import my.notinhas.project.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByUserNameIgnoreCase(String userName);
    Page<Users> findByUserNameContainingIgnoreCase(String userName, Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByUserNameIgnoreCase(String userName);

}
