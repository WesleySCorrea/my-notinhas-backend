package my.notinhas.project.services;

import my.notinhas.project.dtos.request.CommentRequestDTO;

public interface CommentService {

    void saveComment(CommentRequestDTO commentRequestDTO);
}