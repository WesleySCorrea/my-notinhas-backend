package my.notinhas.project.repositories;

import my.notinhas.project.entities.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, String> {

}
