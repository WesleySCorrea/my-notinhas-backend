package my.notinhas.project.services;

import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.PostResponseDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.entities.Posts;

import java.util.List;

public interface IPostService {

    List<PostResponseDTO> findAll();

    PostResponseDTO findByID(Long id);

    PostDTO savePost(PostDTO postDTO);

    PostDTO updatePost(PostDTO postDTO, Long id);

    void deleteByID(Long id);

}
