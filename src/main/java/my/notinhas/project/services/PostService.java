package my.notinhas.project.services;

import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Page<PostResponseDTO> findAll(Pageable pageable);

    PostResponseDTO findByID(Long id);

    PostDTO savePost(PostDTO postDTO);

    PostDTO updatePost(PostDTO postDTO, Long id);

    void deleteByID(Long id);

}
