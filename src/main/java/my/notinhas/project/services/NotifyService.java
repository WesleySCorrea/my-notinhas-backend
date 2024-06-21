package my.notinhas.project.services;

import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.response.NotifyResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotifyService {

    void saveNotify(NotifyDTO notify);

    Page<NotifyResponseDTO> findAllNotifications(Pageable pageable);
}