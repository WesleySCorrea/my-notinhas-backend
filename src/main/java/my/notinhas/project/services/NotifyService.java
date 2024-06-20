package my.notinhas.project.services;

import my.notinhas.project.dtos.response.NotifyResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotifyService {

    Page<NotifyResponseDTO> findAllNotifications(Pageable pageable);
}