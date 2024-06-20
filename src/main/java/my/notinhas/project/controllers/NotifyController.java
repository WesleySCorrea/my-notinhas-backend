package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.response.NotifyResponseDTO;
import my.notinhas.project.services.NotifyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/notify")
public class NotifyController {

    private final NotifyService service;

    public ResponseEntity<Page<NotifyResponseDTO>> findAllNotifications(Pageable pageable) {

        Page<NotifyResponseDTO> notifies = this.service.findAllNotifications(pageable);

        return ResponseEntity.ok().body(null);
    }
}
