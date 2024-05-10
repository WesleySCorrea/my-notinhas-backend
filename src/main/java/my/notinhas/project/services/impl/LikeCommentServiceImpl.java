package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.LikeCommentRequestDTO;
import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.LikeCommentRepository;
import my.notinhas.project.services.LikeCommentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LikeCommentServiceImpl implements LikeCommentService {

    private final LikeCommentRepository repository;

    @Override
    public void saveLike(LikeCommentRequestDTO likeCommentRequestDTO) {
        UserDTO userDTO = extractUser();

        LikesComments existingLike = repository.findByUserIdAndCommentId(userDTO.getId(), likeCommentRequestDTO.getComment().getId());

        if (existingLike != null) {

            if (existingLike.getLikeEnum() == likeCommentRequestDTO.getLikeEnum()) {

                this.repository.deleteById(existingLike.getId());

            } else {
                existingLike.setLikeEnum(likeCommentRequestDTO.getLikeEnum());
                existingLike.setDate(LocalDateTime.now());
                this.repository.save(existingLike);
            }

        } else {

            LikesComments request = likeCommentRequestDTO.converterLikeCommentRequestToLike(userDTO);

            try {
                this.repository.save(request);
            } catch (Exception e) {
                throw new PersistFailedException("Fail when the like was persisted");
            }
        }
    }

    private UserDTO extractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDTO) authentication.getPrincipal();
    }
}