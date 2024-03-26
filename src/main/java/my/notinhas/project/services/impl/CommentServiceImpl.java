package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.CommentRepository;
import my.notinhas.project.services.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

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

    private UserDTO extractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDTO) authentication.getPrincipal();
    }
}