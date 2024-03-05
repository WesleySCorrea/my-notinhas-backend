package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Users;
import my.notinhas.project.repositories.UserRepository;
import my.notinhas.project.services.IUserService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;
    @Autowired
    private  ModelMapper mapper;

    @Override
    public List<UserDTO> findAll() {

        List<Users> users = this.repository.findAll();

        return  users.stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByID(Long id) {

        Users user = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

    var teste = mapper.map(user, UserDTO.class);
        return teste;
    }

    @Override
    public UserDTO saveUsers(UserDTO userDTO) {
        mapper.getConfiguration().setFieldMatchingEnabled(true);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        Users request;
        request = mapper.map(userDTO, Users.class);

        Users newUser = this.repository.save(request);

        return mapper.map(newUser, UserDTO.class);
    }

//    @Override
//    public UserDTO updateUser(UserDTO userRequestDTO, Long id) {
//
////        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
//
//        var userPersisted = this.findByID(id);
//        mapper.map(userRequestDTO, userPersisted);
//        UserDTO userToPersist = mapper.map(userPersisted, UserDTO.class);
//
//        return this.saveUsers(userToPersist);
//    }

    @Override
    public void deleteByID(Long id) {

        this.repository.deleteById(id);
    }
}
