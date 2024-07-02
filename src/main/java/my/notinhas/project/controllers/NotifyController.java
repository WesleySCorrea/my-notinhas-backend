package my.notinhas.project.controllers;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.response.NotifyResponseDTO;
import my.notinhas.project.services.NotifyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/notify")
public class NotifyController {

    private final NotifyService service;

    @GetMapping
    public ResponseEntity<Page<NotifyResponseDTO>> findAllNotifications(Pageable pageable) {

        Page<NotifyResponseDTO> notifies = this.service.findAllNotifications(pageable);

        return ResponseEntity.ok().body(notifies);
    }

    @GetMapping("/quantity")
    public ResponseEntity<Long> countQuantity() {

        Long quantityNotifies = this.service.countNotificationByUserId();

        return ResponseEntity.ok().body(quantityNotifies);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> verifyNotify(@PathVariable Long id) {

        this.service.verifyNotify(id);

        return ResponseEntity.ok().build();
    }
}
