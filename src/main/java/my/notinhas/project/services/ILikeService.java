package my.notinhas.project.services;

import my.notinhas.project.dtos.LikesDTO;
import my.notinhas.project.entities.Likes;

public interface ILikeService {

    Likes saveLikes(LikesDTO likesDTO);

}
