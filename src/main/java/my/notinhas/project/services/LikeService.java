package my.notinhas.project.services;

import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.request.LikeRequestDTO;

public interface LikeService {

    void saveLike(LikeRequestDTO likeRequestDTO);

}