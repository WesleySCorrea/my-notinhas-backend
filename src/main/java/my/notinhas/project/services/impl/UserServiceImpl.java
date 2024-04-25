package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.response.CommentToUserDTO;
import my.notinhas.project.dtos.response.LikeToUserDTO;
import my.notinhas.project.dtos.response.UserHistoryResponseDTO;
import my.notinhas.project.dtos.response.UserIDResponseDTO;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.CommentRepository;
import my.notinhas.project.repositories.LikeRepository;
import my.notinhas.project.repositories.PostRepository;
import my.notinhas.project.repositories.UserRepository;
import my.notinhas.project.services.UserService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;

    @Override
    public List<UserDTO> findAll() {

        List<Users> users = this.repository.findAll();

        return users.stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public  UserDTO findByToken() {
        var user =  this.extractUser();
        user.setGoogleId(null);
        user.setPicture(null);
        return user;
    }

    @Override
    public UserIDResponseDTO findByID(Long id) {

        Users user = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        return new UserIDResponseDTO().converterUserToUserIDResponse(user);
    }

    @Override
    public Page<UserHistoryResponseDTO> findHistoryByUserId(Long id, Pageable pageable) {

        List<UserHistoryResponseDTO> postsUser = this.extratListPostByUser(id);
        List<UserHistoryResponseDTO> commentsUser = this.extratListCommentsByUser(id);
        List<UserHistoryResponseDTO> reactionsUser = this.extratListReactionsByUser(id);

        List<UserHistoryResponseDTO> historyUser = new ArrayList<>();
        historyUser.addAll(postsUser);
        historyUser.addAll(commentsUser);
        historyUser.addAll(reactionsUser);

        historyUser.sort(Comparator.comparing(UserHistoryResponseDTO::getDate).reversed());

        return new PageImpl<>(historyUser, pageable, historyUser.size());
    }

    @Override
    public Page<LikeToUserDTO> findReactionsByUserId(Long id, Pageable pageable) {

        Page<LikeToUserDTO> pageLikes = this.extratPageLikes(id, pageable);

        return pageLikes;
    }

    @Override
    public Page<CommentToUserDTO> findCommentsByUserId(Long id, Pageable pageable) {

        Page<CommentToUserDTO> pageComments = this.extratPageComments(id, pageable);

        return pageComments;
    }

    @Override
    public UserDTO findByEmail(String email) {

        Users user;
        try {
            user = this.repository.findByEmail(email);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }

        return mapper.map(user, UserDTO.class);
    }

    @Override
    public Boolean existsByEmail(String email) {

        return repository.existsByEmail(email);
    }

    @Override
    public UserDTO saveUsers(UserDTO userDTO) {

        Users request = mapper.map(userDTO, Users.class);

        Users newUser;
        try {
            newUser = this.repository.save(request);
        } catch (Exception e) {
            throw new PersistFailedException("Failed when trying to persist the object");
        }

        return mapper.map(newUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Long id) {

        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        var userPersisted = this.findByID(id);
        mapper.map(userDTO, userPersisted);
        UserDTO userToPersist = mapper.map(userPersisted, UserDTO.class);

        return this.saveUsers(mapper.map(userToPersist, UserDTO.class));
    }

    @Override
    public void deleteByID(Long id) {

        UserIDResponseDTO user = this.findByID(id);

        if (user != null) {
            this.repository.deleteById(id);
        } else {
            throw new NoSuchElementException("User with ID: " + id + " not found!");
        }
    }

    private List<UserHistoryResponseDTO> extratListPostByUser(Long userId) {

        List<Posts> posts = this.postRepository.findByUserIdAndActiveIsTrueOrderByDateDesc(userId);

        return posts.stream().map(
            post -> new UserHistoryResponseDTO()
                    .converterPostToUserHistory(post)
        ).collect(Collectors.toList());
    }

    private List<UserHistoryResponseDTO> extratListCommentsByUser(Long userId) {

        List<Comments> comments = this.commentRepository.findByUserIdAndPostActiveIsTrueOrderByDateDesc(userId);

        return comments.stream().map(
                comment -> new UserHistoryResponseDTO()
                        .converterCommentToUserHistory(comment)
        ).collect(Collectors.toList());
    }

    private List<UserHistoryResponseDTO> extratListReactionsByUser(Long userId) {

        List<Likes> likes = this.likeRepository.findByUserIdOrderByDateDesc(userId);

        return likes.stream().map(
                like -> new UserHistoryResponseDTO()
                        .converterReactionToUserHistory(like)
        ).collect(Collectors.toList());
    }

    private Page<CommentToUserDTO> extratPageComments(Long userId, Pageable pageable){

        Page<Comments> commentsList = commentRepository.findByUserIdAndActiveIsTrue(userId, pageable);
        List<CommentToUserDTO> commentToUserDTOList = new ArrayList<>();

        for (Comments comment : commentsList) {
            CommentToUserDTO commentToUserDTO = new CommentToUserDTO()
                    .converterCommentToCommentToUser(comment);
            commentToUserDTOList.add(commentToUserDTO);
        }

        return new PageImpl<>(commentToUserDTOList, pageable, commentsList.getTotalElements());
    }

    private Page<LikeToUserDTO> extratPageLikes(Long userId, Pageable pageable){

        Page<Likes> likesList = likeRepository.findByUserId(userId, pageable);
        List<LikeToUserDTO> likeToUserDTOList = new ArrayList<>();

        for (Likes like : likesList) {
            LikeToUserDTO likeToUserDTO = new LikeToUserDTO()
                    .converterLikeToLikeToUserDTO(like);
            likeToUserDTOList.add(likeToUserDTO);
        }

        return new PageImpl<>(likeToUserDTOList, pageable, likesList.getTotalElements());
    }

    private UserDTO extractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDTO) authentication.getPrincipal();
    }
}