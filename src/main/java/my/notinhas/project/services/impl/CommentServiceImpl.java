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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    @Override
    public List<CommentResponseDTO> findByPostId(Long postId) {

        List<Comments> comments;
        try {
            comments = this.repository.findByPostIdAndParentCommentIsNull(postId);
        } catch (Exception e) {
            throw new ObjectNotFoundException("Comment with post ID " + postId + "not found");
        }

        return comments.stream()
                .map(comment -> new CommentResponseDTO().converterCommentToCommentResponse(comment))
                .collect(Collectors.toList());
    }

    @Override
    public void saveComment(CommentRequestDTO commentRequestDTO) {
        UserDTO userDTO = extractUser();
        commentRequestDTO.setUser(userDTO.convertUserDTOToUser());
        commentRequestDTO.setDate(LocalDateTime.now());

        Comments comments = commentRequestDTO.converterCommentRequestToComment();
        try {
            this.repository.save(comments);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }
    }

    @Override
    public void delete(Long id) {

        this.repository.deleteById(id);
    }

    private UserDTO extractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDTO) authentication.getPrincipal();
    }
}