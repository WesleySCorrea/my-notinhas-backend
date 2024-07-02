package my.notinhas.project.services;

import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.dtos.response.CommentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDTO findById(Long id);
    Page<CommentResponseDTO> findAllCommentSon(Pageable pageable, Long id);
    Page<CommentResponseDTO> findByPostId(Long postId, Pageable pageable);
    Page<CommentResponseDTO> findCommentToNotify(Long postId, Long commentId ,Pageable pageable);
    void saveComment(CommentRequestDTO commentRequestDTO);
    void updateComment(CommentRequestDTO commentRequestDTO, Long id);
    void delete(Long id);
}