package my.notinhas.project.services;

import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.PostResponseDTO;

import java.util.List;

public interface PostService {

    List<PostResponseDTO> findAll();

    PostResponseDTO findByID(Long id);

    PostDTO savePost(PostDTO postDTO);

    PostDTO updatePost(PostDTO postDTO, Long id);

    void deleteByID(Long id);

}
