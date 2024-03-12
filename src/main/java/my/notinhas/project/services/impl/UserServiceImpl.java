package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.component.Requests;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Users;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.repositories.UserRepository;
import my.notinhas.project.services.UserService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final Requests requests;

    @Override
    public List<UserDTO> findAll() {

        List<Users> users = this.repository.findAll();

        return users.stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByID(Long id) {

        Users user = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        return mapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO findByEmail(String email) {

        Users user;
        try {
            user = this.repository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("User with email " + email + " not found.");
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

        UserDTO user = this.findByID(id);

        if (user != null) {
            this.repository.deleteById(id);
        } else {
            throw new NoSuchElementException("User with ID: " + id + " not found!");
        }
    }
}