package my.notinhas.project.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.UpdateUserRequestDTO;
import my.notinhas.project.dtos.response.*;
import my.notinhas.project.entities.*;
import my.notinhas.project.exception.runtime.InvalidUserNameException;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.repositories.*;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;

    @Override
    public List<UserDTO> findAll() {

        List<Users> users = this.repository.findAll();

        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDTO findByUserName(String userName) {

        var user = this.repository.findByUserNameIgnoreCase(userName);
        if (user == null) {
            var notFoundEntity = new Users();
            notFoundEntity.setId(-2L);
            notFoundEntity.setUserName("Usuário: " + userName);
            notFoundEntity.setBio("Não encontrado \uD83E\uDD7A");
            notFoundEntity.setCreated(LocalDateTime.now());
            return new UserProfileDTO().converterUserToUserProfile(notFoundEntity);
        }

        return new UserProfileDTO().converterUserToUserProfile(user);
    }

    @Override
    public UserDTO findByToken() {
        var user = ExtractUser.get();
        user.setGoogleId(null);
        user.setPicture(null);
        return user;
    }

    @Override
    public void updateToken(Long userId, String idToken) {
        this.repository.updateToken(userId, idToken);
    }

    @Override
    public void saveTokenAndRefreshToken(Long userId, String refreshToken, String idToken) {
        this.repository.saveTokenAndRefreshToken(userId, refreshToken, idToken);
    }

    @Override
    public Users findUserByIdToken(String idToken) {
        return this.repository.findByIdToken(idToken);
    }

    @Override
    public void activeUser(String email) {
        this.repository.activeUser(email);
    }


    @Override
    public UserIDResponseDTO findByID(Long id) {

        Users user = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        return new UserIDResponseDTO().converterUserToUserIDResponse(user);
    }

    @Override
    public Page<UserProfileDTO> findByUserNameContaining(String userName, Pageable pageable) {

        if (userName.length() < 3) {
            return null;
        }

        Page<Users> users;
        try {
            users = this.repository.findByUserNameContainingIgnoreCaseAndActiveTrue(userName, pageable);
        } catch (Exception e) {
            throw new ObjectNotFoundException("User with username " + userName + " not found.");
        }
        List<UserProfileDTO> userProfiles = users.stream()
                .map(user -> new UserProfileDTO().converterUserToUserProfile(user))
                .toList();

        return new PageImpl<>(userProfiles, pageable, users.getTotalElements());
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
    public Users findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {

        return this.repository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUserName(String userName) {

        return this.repository.existsByUserNameIgnoreCase(userName);
    }

    @Override
    public Users saveUsers(GoogleIdToken googleIdToken) {
        var persistUser = new UserDTO().createUserDTOWithUserName(googleIdToken);
        return this.repository.save(persistUser);
    }

    @Override
    public UserDTO updateUser(UpdateUserRequestDTO updateUserRequestDTO) {

        if (updateUserRequestDTO.getUserName() != null &&!isValidUsername(updateUserRequestDTO.getUserName())) {
            throw new InvalidUserNameException("Invalid username");
        }

        var user = ExtractUser.get();

        Users userPersisted;
        try {
            userPersisted = this.repository.findByUserNameIgnoreCase(user.getUserName());
        } catch (Exception e) {
            throw new ObjectNotFoundException("User with username " + user.getUserName() + " not found.");
        }

        if (updateUserRequestDTO.getUserName() != null) {
            userPersisted.setUserName(updateUserRequestDTO.getUserName());
            userPersisted.setEditatedUsername(LocalDateTime.now());
        }
        if (updateUserRequestDTO.getBio() != null) {
            userPersisted.setBio(updateUserRequestDTO.getBio());
            userPersisted.setEditatedBio(LocalDateTime.now());
        }

        var savedUser = this.repository.save(userPersisted);

        return new UserDTO(savedUser);
    }

    @Override
    public void delete() {
        UserDTO userDTO = ExtractUser.get();

        List<Comments> comments = this.commentRepository.findByUserUserNameAndActiveIsTrue(userDTO.getUserName());

        for (Comments comment : comments) {
            comment.setActive(false);
            this.commentRepository.save(comment);
        }

        List<Posts> posts = this.postRepository.findByUserUserNameAndActiveIsTrue(userDTO.getUserName());

        for (Posts post : posts) {
            post.setActive(false);
            this.postRepository.save(post);
        }

        List<Likes> likes = this.likeRepository.findByUserUserName(userDTO.getUserName());

        for (Likes like : likes) {
            this.likeRepository.delete(like);
        }

        List<LikesComments> likesComments = this.likeCommentRepository.findByUserUserName(userDTO.getUserName());

        for (LikesComments likeComment : likesComments) {
            this.likeCommentRepository.delete(likeComment);
        }

        String restUserName = this.resetUserName(userDTO.getGoogleId(), userDTO.getFirstName());
        this.repository.updateUserFields(userDTO.getUserId(), restUserName);
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

    private Page<CommentToUserDTO> extratPageComments(Long userId, Pageable pageable) {

        Page<Comments> commentsList = commentRepository.findByUserIdAndActiveIsTrue(userId, pageable);
        List<CommentToUserDTO> commentToUserDTOList = new ArrayList<>();

        for (Comments comment : commentsList) {
            CommentToUserDTO commentToUserDTO = new CommentToUserDTO()
                    .converterCommentToCommentToUser(comment);
            commentToUserDTOList.add(commentToUserDTO);
        }

        return new PageImpl<>(commentToUserDTOList, pageable, commentsList.getTotalElements());
    }

    private Page<LikeToUserDTO> extratPageLikes(Long userId, Pageable pageable) {

        Page<Likes> likesList = likeRepository.findByUserId(userId, pageable);
        List<LikeToUserDTO> likeToUserDTOList = new ArrayList<>();

        for (Likes like : likesList) {
            LikeToUserDTO likeToUserDTO = new LikeToUserDTO()
                    .converterLikeToLikeToUserDTO(like);
            likeToUserDTOList.add(likeToUserDTO);
        }

        return new PageImpl<>(likeToUserDTOList, pageable, likesList.getTotalElements());
    }


    private static boolean isValidUsername(String username) {

        String regex = "^[^\\sçáéíóúàèìòùâêîôûãõäëïöü]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }

    private String resetUserName(String googleId, String firstName) {

        char number1 = googleId.charAt(3);
        char number2 = googleId.charAt(9);
        char number3 = googleId.charAt(11);
        char number4 = googleId.charAt(15);
        char number5 = googleId.charAt(20);

        String userName = firstName + number1 + number2 + number3 + number4 + number5;

        return userName;
    }
}
