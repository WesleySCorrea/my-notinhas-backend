package my.notinhas.project.services;

import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.response.CommentToUserDTO;
import my.notinhas.project.dtos.response.LikeToUserDTO;
import my.notinhas.project.dtos.response.UserHistoryResponseDTO;
import my.notinhas.project.dtos.response.UserIDResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserIDResponseDTO findByID(Long id);
    Page<UserHistoryResponseDTO> findHistoryByUserId(Long id, Pageable pageable);
    Page<LikeToUserDTO> findReactionsByUserId(Long id, Pageable pageable);
    Page<CommentToUserDTO> findCommentsByUserId(Long id, Pageable pageable);
    UserDTO findByEmail(String email);

    Boolean existsByEmail(String email);

    UserDTO saveUsers(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO, Long id);

    void deleteByID(Long id);

}
