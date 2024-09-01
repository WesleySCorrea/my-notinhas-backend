package my.notinhas.project.services;

import my.notinhas.project.dtos.request.PostRequestDTO;
import my.notinhas.project.dtos.response.PostPublicResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Page<PostPublicResponseDTO> findAllPublic(Pageable pageable);
    Page<PostResponseDTO> findAll(Pageable pageable);
    Page<PostResponseDTO> findByCommunityId(Long communityId,Pageable pageable);

    Page<PostResponseDTO> findByUserId(Long userId,Pageable pageable);
    Page<PostResponseDTO> searchPosts(Pageable pageable,String content);
    PostResponseDTO findByID(Long id);
    void savePost(PostRequestDTO postRequestDTO);
    void updatePost(PostRequestDTO postRequestDTO, Long id);
    void deleteByID(Long id);

}
