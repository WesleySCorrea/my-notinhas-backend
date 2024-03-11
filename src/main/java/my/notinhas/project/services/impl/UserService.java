package my.notinhas.project.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.AllArgsConstructor;
import my.notinhas.project.component.Requests;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.auth.IdTokenDTO;
import my.notinhas.project.entities.Users;
import my.notinhas.project.repositories.UserRepository;
import my.notinhas.project.services.IUserService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

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
    public UserDTO saveUsers(UserDTO userDTO) {

        Users request = mapper.map(userDTO, Users.class);

        Users newUser = this.repository.save(request);

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

    @Override
    public IdTokenDTO validateIdToken(String access_token) {

        IdTokenDTO idTokenDTO = requests.idTokenRequest(access_token);
        if (idTokenDTO == null) {
            System.out.println("Invalid access_token");
            return null;
        }

        String CLIENT_ID = "291704101729-tu7ghdk6ccd08f78rugpq8o22ji89jv3.apps.googleusercontent.com";

        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenDTO.getId_token());
            if (idToken != null) {
                System.out.println("Id_token Valided");
                return idTokenDTO;
            } else {
                System.out.println("Invalid Id_token");
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}