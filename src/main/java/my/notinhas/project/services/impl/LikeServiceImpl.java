package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.LikeRequestDTO;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Users;
import my.notinhas.project.repositories.LikeRepository;
import my.notinhas.project.services.LikeService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository repository;
    private final ModelMapper mapper;

    @Override
    public LikeDTO saveLike(LikeRequestDTO likeRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        likeRequestDTO.setUser(userDTO);

        Likes existingLike = repository.findByUserIdAndPostId(userDTO.getId(), likeRequestDTO.getPost().getId());

        Likes newLike = new Likes();
        if (existingLike != null) {

            if (existingLike.getLikeEnum().equals(likeRequestDTO.getLikeEnum())) {
                throw new RuntimeException("Usuario, já votou");
            } else {
                existingLike.setLikeEnum(likeRequestDTO.getLikeEnum());
                newLike = repository.save(existingLike);
            }

        } else {
            Likes request = mapper.map(likeRequestDTO, Likes.class);

            newLike = this.repository.save(request);
        }

        return mapper.map(newLike, LikeDTO.class);
    }
}