package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.PostResponseDTO;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.repositories.PostRepository;
import my.notinhas.project.services.PostService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final ModelMapper mapper;

    @Override
    public Page<PostResponseDTO> findAll(Pageable pageable) {

        Page<Posts> posts = this.repository.findAll(pageable);

        for (Posts post: posts) {
            post.calculateTotalLikesAndDeslikes(post.getLikes());
        }

        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> mapper.map(post, PostResponseDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public PostResponseDTO findByID(Long id) {

        Posts post = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post with ID " + id + " not found."));

        post.calculateTotalLikesAndDeslikes(post.getLikes());


        return mapper.map(post, PostResponseDTO.class);
    }

    @Override
    public PostDTO savePost(PostDTO postDTO) {

        Posts request = mapper.map(postDTO, Posts.class);

        Posts newPost = this.repository.save(request);

        return mapper.map(newPost, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id) {

        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        var postPersisted = this.findByID(id);
        mapper.map(postDTO, postPersisted);
        PostDTO postToPersist = mapper.map(postPersisted, PostDTO.class);

        return this.savePost(mapper.map(postToPersist, PostDTO.class));
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