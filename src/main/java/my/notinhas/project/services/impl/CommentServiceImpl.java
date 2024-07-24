package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.NotifyDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.CommentRequestDTO;
import my.notinhas.project.dtos.response.CommentResponseDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.LikesComments;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.ActionEnum;
import my.notinhas.project.enums.LikeEnum;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.repositories.CommentRepository;
import my.notinhas.project.repositories.LikeCommentRepository;
import my.notinhas.project.services.CommentService;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.NotifyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final NotifyService notifyService;
    private final CommentRepository repository;
    private final LikeCommentRepository likeCommentRepository;

    @Override
    public CommentResponseDTO findById(Long id) {
        UserDTO userDTO = ExtractUser.get();

        Comments comment = this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Comment with ID " + id + " not found."));

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO().converterCommentToCommentResponse(comment, userDTO);
        commentResponseDTO.setTotalLikes(this.calculeTotalLike(comment.getId()));
        commentResponseDTO.setUserLike(this.verifyUserLike(commentResponseDTO, userDTO));

        for (CommentResponseDTO replies : commentResponseDTO.getReplies()) {

            replies.setTotalLikes(this.calculeTotalLike(replies.getId()));
            replies.setUserLike(this.verifyUserLike(replies, userDTO));
        }

        return commentResponseDTO;
    }

    @Override
    public Page<CommentResponseDTO> findAllCommentSon(Pageable pageable, Long id) {
        UserDTO userDTO = ExtractUser.get();

        Page<Comments> comments;
        try {
            comments = this.repository.findAllByParentCommentIdAndActiveIsTrue(pageable, id);
        } catch (Exception e) {
            throw new ObjectNotFoundException("Comments not found");
        }
        List<CommentResponseDTO> commentResponseDTOS = comments.stream()
                .map(comment -> {
                    CommentResponseDTO commentResponseDTO = new CommentResponseDTO()
                            .converterCommentToCommentResponse(comment, userDTO);

                    commentResponseDTO.setTotalLikes(this.calculeTotalLike(comment.getId()));
                    commentResponseDTO.setUserLike(this.verifyUserLike(commentResponseDTO, userDTO));
                    return commentResponseDTO;
                })
                .toList();

        return new PageImpl<>(commentResponseDTOS, pageable, comments.getTotalElements());
    }

    @Override
    public Page<CommentResponseDTO> findByPostId(Long postId, Pageable pageable) {

        UserDTO userDTO = ExtractUser.get();

//        Page<Comments> comments;
//        try {
//            comments = this.repository.findByPostIdAndParentCommentIsNullAndActiveIsTrue(postId, pageable);
//        } catch (Exception e) {
//            throw new ObjectNotFoundException("Comment with post ID " + postId + "not found");
//        }
//        List<CommentResponseDTO> commentResponseDTOS = comments.stream()
//                .map(comment -> {
//                    CommentResponseDTO commentResponseDTO = new CommentResponseDTO()
//                            .converterCommentToCommentResponse(comment, userDTO);
//
//                    commentResponseDTO.setTotalLikes(this.calculeTotalLike(comment.getId()));
//                    commentResponseDTO.setUserLike(this.verifyUserLike(commentResponseDTO, userDTO));
//
//                    for (CommentResponseDTO replies : commentResponseDTO.getReplies()) {
//
//                        replies.setTotalLikes(this.calculeTotalLike(replies.getId()));
//                        replies.setUserLike(this.verifyUserLike(replies, userDTO));
//                    }
//
//                    return commentResponseDTO;
//                })
//                .toList();
//
//        return new PageImpl<>(commentResponseDTOS, pageable, comments.getTotalElements());
        Page<Object[]> comments;
        try {
            comments = this.repository.findByPostIdAndParentCommentIsNull(postId, userDTO.getUserId(), pageable);
        } catch (Exception e) {
            throw new ObjectNotFoundException("Comments with post ID " + postId + "not found");
        }

        List<CommentResponseDTO> dtos = comments.stream()
                .map(CommentResponseDTO::new)
                .toList();

        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    @Override
    public Page<CommentResponseDTO> findCommentToNotify(Long postId, Long commentId, Pageable pageable) {

        Page<CommentResponseDTO> comments = this.findByPostId(postId, pageable);

        if (commentId != 0) {
            List<CommentResponseDTO> commentsList = new ArrayList<>(comments.getContent());

            for (CommentResponseDTO comment : commentsList) {
                if (comment.getId().equals(commentId)) {
                    commentsList.remove(comment);
                    commentsList.add(0, comment);
                    return new PageImpl<>(commentsList, pageable, comments.getTotalElements());
                }
            }

            CommentResponseDTO commentToNotify = this.findById(commentId);
            commentsList.add(0, commentToNotify);
            return new PageImpl<>(commentsList, pageable, comments.getTotalElements());
        }
        return comments;
    }

    @Override
    public CommentResponseDTO saveComment(CommentRequestDTO commentRequestDTO) {
        UserDTO userDTO = ExtractUser.get();

        if (commentRequestDTO.getParentCommentId() != null) {
            Long fatherCommentId = commentRequestDTO.getParentCommentId();

//RETURAR ESSE ORELSETHROW
            Comments fatherComment = this.repository.findById(fatherCommentId)
                    .orElseThrow(() -> new ObjectNotFoundException("Comment with ID " + fatherCommentId + " not found."));
            if (fatherComment.getParentComment() != null) {
                commentRequestDTO.setParentCommentId(fatherComment.getParentComment().getId());
            }
        }

        Comments comments = commentRequestDTO.converterCommentRequestToComment(LocalDateTime.now(), userDTO);
        try {
            Comments commentPersisted = this.repository.save(comments);
            this.createNotification(commentPersisted, userDTO, commentRequestDTO.getPostOwnerId());
            return new CommentResponseDTO().converterCommentToCommentResponse(commentPersisted, userDTO);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }

    }

    @Override
    public void updateComment(CommentRequestDTO commentRequestDTO, Long id) {

        UserDTO user = ExtractUser.get();

        Comments commentPersisted = this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Comment with ID " + id + " not found."));


        if (commentPersisted.getUser().getUserName().equals(user.getUserName())) {
            commentPersisted.setContent(commentRequestDTO.getContent());
            commentPersisted.setDate(LocalDateTime.now());
            commentPersisted.setIsEdited(Boolean.TRUE);

            this.repository.save(commentPersisted);
        } else {
            throw new UnauthorizedIdTokenException("Comment does not belong to the user");
        }
    }

    @Override
    public void delete(Long id) {
        UserDTO userDTO = ExtractUser.get();

        Comments comment = this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Comment with ID " + id + " not found."));

        if (comment.getUser().getUserName().equals(userDTO.getUserName())) {
            comment.setActive(Boolean.FALSE);
            this.repository.deactivateCommentsByParentId(id);
            this.repository.save(comment);
        }
        this.removeNotification(comment, userDTO.getUserId());
    }


    private Long calculeTotalLike(Long commentId) {

        Long totalLike = likeCommentRepository.countByCommentIdAndLikeEnum(commentId, LikeEnum.LIKE);
        Long totalDislike = likeCommentRepository.countByCommentIdAndLikeEnum(commentId, LikeEnum.DISLIKE);

        return totalLike - totalDislike;
    }

    private LikeEnum verifyUserLike(CommentResponseDTO comments, UserDTO user) {

        LikesComments like = likeCommentRepository.findByUserIdAndCommentId(user.getUserId(), comments.getId());

        if (like == null) {
            return null;
        }
        return like.getLikeEnum();
    }

    private void createNotification(Comments comments, UserDTO userDTO, Long postOwnerId) {

        ActionEnum actionEnum;
        Long replieId = null;

        if (comments.getParentComment() == null) {
            actionEnum = ActionEnum.COMMENT_IN_POST;
        } else {
            actionEnum = ActionEnum.COMMENT_IN_COMMENT;
            replieId = comments.getParentComment().getId();
        }

        Users notifyOwner = new Users();
        notifyOwner.setId(postOwnerId);

        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setNotifyOwner(notifyOwner);
        notifyDTO.setUser(userDTO.convertUserDTOToUser());
        notifyDTO.setPost(comments.getPost());
        notifyDTO.setComment(comments);
        notifyDTO.setParentId(replieId);
        notifyDTO.setActionEnum(actionEnum);
        notifyDTO.setVerified(Boolean.FALSE);
        notifyDTO.setDate(LocalDateTime.now());

        this.notifyService.saveNotify(notifyDTO, userDTO.getUserId());
    }

    private void removeNotification(Comments comments, Long userId) {

        ActionEnum actionEnum;

        if (comments.getParentComment() == null) {
            actionEnum = ActionEnum.COMMENT_IN_POST;
        } else {
            actionEnum = ActionEnum.COMMENT_IN_COMMENT;
        }

        this.notifyService.removeNotificationOfComment(comments, userId, actionEnum);
    }
}