package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.dtos.response.CommentResponseDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.CommentRepository;
import my.notinhas.project.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    @Override
    public Page<CommentResponseDTO> findByPostId(Long postId, Pageable pageable) {

        UserDTO userDTO = extractUser();

        Page<Comments> comments;
        try {
            comments = this.repository.findByPostIdAndParentCommentIsNull(postId, pageable);
        } catch (Exception e) {
            throw new ObjectNotFoundException("Comment with post ID " + postId + "not found");
        }
        List<CommentResponseDTO> commentResponseDTOS = comments.stream()
                .map(comment ->
                        new CommentResponseDTO()
                                .converterCommentToCommentResponse(comment, userDTO))
                .toList();

        return new PageImpl<>(commentResponseDTOS, pageable, comments.getTotalElements());
    }

    @Override
    public void saveComment(CommentRequestDTO commentRequestDTO) {
        UserDTO userDTO = extractUser();
        commentRequestDTO.setUser(userDTO.convertUserDTOToUser());
        commentRequestDTO.setDate(LocalDateTime.now());

        if (commentRequestDTO.getParentComment() != null) {
            Long fatherCommentId = commentRequestDTO.getParentComment().getId();

            Comments fatherComment = this.repository.findById(fatherCommentId)
                    .orElseThrow(() -> new ObjectNotFoundException("Comment with ID " + fatherCommentId + " not found."));
            if (fatherComment.getParentComment() != null) {
                commentRequestDTO.setParentComment(fatherComment.getParentComment());
            }
        }

        Comments comments = commentRequestDTO.converterCommentRequestToComment();
        try {
            this.repository.save(comments);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }
    }

    @Override
    public void delete(Long id) {
        UserDTO userDTO = extractUser();

        Comments comment = this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Comment with ID " + id + " not found."));

        if (comment.getUser().getUserName().equals(userDTO.getUserName())) {
            this.repository.deleteById(id);
        }
    }

    private UserDTO extractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDTO) authentication.getPrincipal();
    }
}