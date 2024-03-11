package my.notinhas.project.services;

import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.LikeResponseDTO;

import java.util.List;

public interface LikeService {
    List<LikeResponseDTO> findAll();

    LikeResponseDTO findByID(Long id);

    LikeDTO saveLike(LikeDTO likeDTO);

    LikeDTO updateLike(LikeDTO likeDTO, Long id);

    void deleteByID(Long id);

}
