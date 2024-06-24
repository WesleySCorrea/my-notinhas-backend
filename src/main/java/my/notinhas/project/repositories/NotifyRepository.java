package my.notinhas.project.repositories;

import my.notinhas.project.entities.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {

    Page<Notify> findAllByNotifyOwnerIdAndVerifiedFalseOrderByDateDesc(Long notifyOwner, Pageable pageable);
}