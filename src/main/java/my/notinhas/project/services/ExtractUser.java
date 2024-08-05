package my.notinhas.project.services;

import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ExtractUser {

    public static UserDTO get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var users =  (Users) authentication.getPrincipal();
        return new UserDTO(users);
    }
}
