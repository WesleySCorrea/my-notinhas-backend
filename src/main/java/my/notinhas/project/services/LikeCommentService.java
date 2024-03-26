package my.notinhas.project.services;

import my.notinhas.project.dtos.request.LikeCommentRequestDTO;

public interface LikeCommentService {

    void saveLike(LikeCommentRequestDTO likeCommentRequestDTO);

}