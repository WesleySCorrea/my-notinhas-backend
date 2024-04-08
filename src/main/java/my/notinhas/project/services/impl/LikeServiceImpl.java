package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.LikeRequestDTO;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.LikeRepository;
import my.notinhas.project.services.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository repository;

    @Override
    public void saveLike(LikeRequestDTO likeRequestDTO) {
        UserDTO userDTO = extractUser();
        likeRequestDTO.setUser(userDTO.convertUserDTOToUser());

        Likes existingLike = repository.findByUserIdAndPostId(userDTO.getId(), likeRequestDTO.getPost().getId());

        Likes newLike;
        if (existingLike != null) {

            if (existingLike.getLikeEnum() == likeRequestDTO.getLikeEnum()) {

                this.repository.deleteById(existingLike.getId());

            } else {
                existingLike.setLikeEnum(likeRequestDTO.getLikeEnum());
                existingLike.setDate(LocalDateTime.now());
                this.repository.save(existingLike);
            }

        } else {

            Likes request = likeRequestDTO.converterLikeRequestToLike();

            try {
                request.setDate(LocalDateTime.now());
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