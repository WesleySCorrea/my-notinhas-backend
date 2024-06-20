package my.notinhas.project.repositories;

import my.notinhas.project.entities.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
}
