package my.notinhas.project.repositories;

import my.notinhas.project.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);

}
