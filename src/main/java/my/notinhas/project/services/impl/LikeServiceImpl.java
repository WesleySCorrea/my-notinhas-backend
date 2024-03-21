package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.request.LikeRequestDTO;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.repositories.LikeRepository;
import my.notinhas.project.services.LikeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository repository;
    private final ModelMapper mapper;

    @Override
    public LikeDTO saveLike(LikeRequestDTO likeRequestDTO) {

        Likes request = mapper.map(likeRequestDTO, Likes.class);

        Likes newLike = this.repository.save(request);

        return mapper.map(newLike, LikeDTO.class);
    }
}