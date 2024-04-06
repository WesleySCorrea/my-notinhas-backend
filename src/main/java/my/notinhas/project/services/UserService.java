package my.notinhas.project.services;

import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.dtos.response.UserIDResponseDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserIDResponseDTO findByID(Long id);

    UserDTO findByEmail(String email);

    Boolean existsByEmail(String email);

    UserDTO saveUsers(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO, Long id);

    void deleteByID(Long id);

}
