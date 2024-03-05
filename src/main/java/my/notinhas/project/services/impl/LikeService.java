package my.notinhas.project.services.impl;

import my.notinhas.project.entities.Likes;
import my.notinhas.project.dtos.LikesDTO;
import my.notinhas.project.services.ILikeService;
import org.springframework.stereotype.Service;

@Service
public class LikeService implements ILikeService {
    @Override
    public Likes saveLikes(LikesDTO likesDTO) {
        return null;
    }
}
