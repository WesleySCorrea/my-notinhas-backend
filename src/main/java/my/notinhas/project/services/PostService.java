package my.notinhas.project.services;

import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.request.PostRequestDTO;
import my.notinhas.project.dtos.response.PostPublicResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Page<PostPublicResponseDTO> findAllPublic(Pageable pageable);
    Page<PostResponseDTO> findAll(Pageable pageable);
    PostResponseDTO findByID(Long id);
    PostDTO savePost(PostRequestDTO postRequestDTO);
    PostDTO updatePost(PostRequestDTO postRequestDTO, Long id);
    void deleteByID(Long id);

}
