package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.PostRequestDTO;
import my.notinhas.project.dtos.response.PostPublicResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.repositories.PostRepository;
import my.notinhas.project.services.PostService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final ModelMapper mapper;

    @Override
    public Page<PostPublicResponseDTO> findAllPublic(Pageable pageable) {
        Page<Posts> posts = this.repository.findAll(pageable);

        List<PostPublicResponseDTO> postPublicResponseDTO = posts.stream()
                .map(post -> mapper.map(post, PostPublicResponseDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(postPublicResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public Page<PostResponseDTO> findAll(Pageable pageable) {

        Page<Posts> posts = this.repository.findAll(pageable);

        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> mapper.map(post, PostResponseDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public PostResponseDTO findByID(Long id) {

        Posts post = this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Post with ID " + id + " not found."));

        return mapper.map(post, PostResponseDTO.class);
    }

    @Override
    public PostDTO savePost(PostRequestDTO postRequestDTO) {

        Posts request = new Posts();
        request.setDate(LocalDateTime.now());
        request.setContent(postRequestDTO.getContent());
        request.setActive(Boolean.TRUE);
        request.setUser(mapper.map(postRequestDTO.getUser(), Users.class));

        Posts newPost = this.repository.save(request);

        return mapper.map(newPost, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostRequestDTO postRequestDTO, Long id) {

        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        var postPersisted = this.findByID(id);
        postPersisted.setContent(postRequestDTO.getContent());

        Posts postToPersist;
        if (postPersisted.getUser().getId().equals(postRequestDTO.getUser().getId())) {
            postToPersist = repository.save(mapper.map(postPersisted, Posts.class));
        } else {
            throw new RuntimeException("Post não pertence ao usuário");
        }

        return this.savePost(mapper.map(postToPersist, PostRequestDTO.class));
    }

    @Override
    public void deleteByID(Long id) {

        PostResponseDTO post = this.findByID(id);

        if (post!=null) {
            this.repository.deleteById(id);
        } else {
            throw new NoSuchElementException("Post with ID: " + id + " not found!");
        }
    }
}