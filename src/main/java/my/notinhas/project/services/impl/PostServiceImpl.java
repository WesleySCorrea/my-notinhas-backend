package my.notinhas.project.services.impl;

import lombok.RequiredArgsConstructor;
import my.notinhas.project.component.Variables;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.PostRequestDTO;
import my.notinhas.project.dtos.response.PostPublicResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.repositories.PostRepository;
import my.notinhas.project.services.ExtractUser;
import my.notinhas.project.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final Variables variables;
    private final PostRepository postRepository;
    @Override
    public Page<PostPublicResponseDTO> findAllPublic(Pageable pageable) {

        Page<Object[]> posts = this.postRepository
                .findAllPostsByPublic(pageable);

        List<PostPublicResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    PostPublicResponseDTO postsResponseDTO = new PostPublicResponseDTO(post);

                    if (variables.getDeleteAfterOneDay()) {
                        this.verifyActivePost(postsResponseDTO);
                    }
                    return postsResponseDTO;
                })
                .toList();

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public Page<PostResponseDTO> findAll(Pageable pageable) {
        UserDTO user = ExtractUser.get();

        Page<Object[]> posts = this.postRepository.findActivePosts(user.getUserId(), pageable);

        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    PostResponseDTO dto = new PostResponseDTO(post);

                    if (variables.getDeleteAfterOneDay()) {
                        this.verifyActivePost(dto);
                    }
                    return dto;
                })
                .toList();

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public Page<PostResponseDTO> searchPosts(Pageable pageable, String content) {
        UserDTO user = ExtractUser.get();

        Page<Object[]> posts = this.postRepository.findActivePostsContaining(user.getUserId(), content, pageable);

        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    PostResponseDTO dto = new PostResponseDTO(post);

                    if (variables.getDeleteAfterOneDay()) {
                        this.verifyActivePost(dto);
                    }
                    return dto;
                })
                .toList();

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public PostResponseDTO findByID(Long postId) {
        UserDTO user = ExtractUser.get();

        List<Object[]> posts = this.postRepository.findActivePostById(user.getUserId(), postId);

        var postResponseDTO = posts.stream()
                .map(PostResponseDTO::new)
                .toList();

        return postResponseDTO.get(0);
    }

    @Override
    public void savePost(PostRequestDTO postRequestDTO) {
        UserDTO user = ExtractUser.get();

        String filteredContent = postRequestDTO.getContent();
        filteredContent = filteredContent.replaceAll("\\n+", "\n");

        Posts request = new Posts();
        request.setDate(LocalDateTime.now());
        request.setContent(filteredContent);
        request.setActive(Boolean.TRUE);
        request.setUser(user.convertUserDTOToUser());

        try {
            this.postRepository.save(request);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }
    }

    @Override
    public void updatePost(PostRequestDTO postRequestDTO, Long id) {
        UserDTO user = ExtractUser.get();

        PostResponseDTO postPersisted = this.findByID(id);

        if (postPersisted.getUser().getUserName().equals(user.getUserName())) {

            int updatedRows = postRepository.updatePostContentAndDate(
                    postPersisted.getId(),
                    postRequestDTO.getContent(),
                    LocalDateTime.now(),
                    Boolean.TRUE
            );

            if (updatedRows == 0) {
                throw new ObjectNotFoundException("Post not found or update failed");
            }
        } else {
            throw new UnauthorizedIdTokenException("Post does not belong to the user");
        }
    }

    @Override
    public void deleteByID(Long id) {
        var user = ExtractUser.get();
        var post = this.postRepository.findById(id);
        if (post.isPresent() && post.get().getUser().getEmail().equals(user.getEmail())) {
            var postToDelete = post.get();
                postToDelete.setActive(Boolean.FALSE);
                this.postRepository.save(postToDelete);
        } else {
            throw new NoSuchElementException("Post with ID: " + id + " not found!");
        }
    }

    private void verifyActivePost (PostResponseDTO post) {

        Duration duration = Duration.between(post.getDate(), LocalDateTime.now());

        if (duration.toHours() >= 24) {
            int rowsAffected = postRepository.updateActiveFalse(post.getId());
            if (rowsAffected == 0) {
                System.out.println("Post with ID: " + post.getId() + " not found!");
            }
        }
    }

    private void verifyActivePost (PostPublicResponseDTO post) {

        Duration duration = Duration.between(post.getDate(), LocalDateTime.now());

        if (duration.toHours() >= 24) {
            int rowsAffected = postRepository.updateActiveFalse(post.getId());
            if (rowsAffected == 0) {
                System.out.println("Post with ID: " + post.getId() + " not found!");
            }
        }
    }
}