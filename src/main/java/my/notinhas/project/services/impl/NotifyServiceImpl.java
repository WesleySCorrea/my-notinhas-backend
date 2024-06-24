package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.response.NotifyResponseDTO;
import my.notinhas.project.entities.Notify;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.NotifyRepository;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.NotifyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final NotifyRepository NotifyRepository;

    @Override
    public void saveNotify(NotifyDTO notify) {

        try {
            this.NotifyRepository.save(notify.converterNotifyDTOToNotify());
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }
    }

    @Override
    public Page<NotifyResponseDTO> findAllNotifications(Pageable pageable) {
        UserDTO user = ExtractUser.get();

        Page<Notify> notifies = this.NotifyRepository.findAllByNotifyOwnerIdAndVerifiedFalseOrderByDateDesc(user.getUserId(), pageable);

        List<NotifyResponseDTO> notifyResponseDTOS = notifies.stream()
                .map(notify -> {
                    NotifyResponseDTO notifyDTO = new NotifyResponseDTO();
                    return notifyDTO.converterNotifyToNotifyResponseDTO(notify);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(notifyResponseDTOS, pageable, notifies.getTotalElements());
    }
}