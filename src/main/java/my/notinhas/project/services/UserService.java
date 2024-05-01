package my.notinhas.project.services;

import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.request.UpdateUserRequestDTO;
import my.notinhas.project.dtos.response.*;
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
    UserDTO findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUserName(String userName);
    UserDTO saveUsers(UserDTO userDTO);

    UserDTO updateUser(UpdateUserRequestDTO updateUserRequestDTO);
    void delete();
    UserDTO findByToken();

}
