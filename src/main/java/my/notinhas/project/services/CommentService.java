package my.notinhas.project.services;

import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.dtos.response.CommentResponseDTO;
import my.notinhas.project.entities.Comments;

import java.util.List;

public interface CommentService {
    List<CommentResponseDTO> findByPostId(Long postId);
    void saveComment(CommentRequestDTO commentRequestDTO);
    void delete(Long id);
}