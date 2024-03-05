package my.notinhas.project.services;

import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.entities.Posts;

public interface IPostService {

    Posts saveUsers(PostDTO postDTO);

}
