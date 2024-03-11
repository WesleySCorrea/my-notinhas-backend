package my.notinhas.project.services;

import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;

import java.util.List;

public interface IUserService {

    List<UserDTO> findAll();

    UserDTO findByID(Long id);

    UserDTO saveUsers(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO, Long id);

    void deleteByID(Long id);

    IdTokenDTO validateIdToken(String idToken);
}
