package my.notinhas.project.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.UpdateUserRequestDTO;
import my.notinhas.project.dtos.response.*;
import my.notinhas.project.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();
    UserProfileDTO findByUserName(String userName);
    UserIDResponseDTO findByID(Long id);
    Page<UserProfileDTO> findByUserNameContaining(String userName, Pageable pageable);
    Page<UserHistoryResponseDTO> findHistoryByUserId(Long id, Pageable pageable);
    Page<LikeToUserDTO> findReactionsByUserId(Long id, Pageable pageable);
    Page<CommentToUserDTO> findCommentsByUserId(Long id, Pageable pageable);
    Users findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUserName(String userName);
    Users saveUsers(GoogleIdToken googleIdToken);

    UserDTO updateUser(UpdateUserRequestDTO updateUserRequestDTO);
    void delete();
    UserDTO findByToken();

    void updateToken(Long userId, String idToken);

    void saveTokenAndRefreshToken(Long userId, String refreshToken, String idToken);

    Users findUserByIdToken(String idToken);

    void activeUser(String email);


}
