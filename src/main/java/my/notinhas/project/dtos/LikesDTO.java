package my.notinhas.project.dtos;

import my.notinhas.project.entities.Posts;
import my.notinhas.project.enums.Value;

public record LikesDTO (
        Value value,
        Posts post
){
}