package my.notinhas.project.dtos;

import my.notinhas.project.entities.Users;

import java.time.LocalDate;

public record PostDTO(
        String userName,
        LocalDate date,
        String content,
        Boolean active,
        Users user
) {
}