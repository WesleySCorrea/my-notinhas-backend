package my.notinhas.project.services.impl;

import lombok.RequiredArgsConstructor;
import my.notinhas.project.component.Variables;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.PostRequestDTO;
import my.notinhas.project.dtos.response.PostIDResponseDTO;
import my.notinhas.project.dtos.response.PostPublicResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.enums.LikeEnum;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.repositories.CommentRepository;
import my.notinhas.project.repositories.LikeRepository;
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
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    @Override
    public Page<PostPublicResponseDTO> findAllPublic(Pageable pageable) {

        Page<Posts> posts = this.postRepository
                .findAllByActiveTrueOrderByDateDesc(pageable);

        List<PostPublicResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    if (variables.getDeleteAfterOneDay()) {
                        this.verifyDateActive(post);
                    }

                    return new PostPublicResponseDTO(post);
                })
                .toList();

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public Page<PostResponseDTO> findAll(Pageable pageable) {
        UserDTO user = ExtractUser.get();

        Page<Posts> posts = this.postRepository
                .findAllByActiveTrueOrderByDateDesc(pageable);

        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    if (variables.getDeleteAfterOneDay()) {
                        this.verifyDateActive(post);
                    }

                    PostResponseDTO dto = new PostResponseDTO(post);

                    dto.setPostOwner(this.verifyPostOwner(user,post));
                    dto.setUserLike(this.verifyUserLike(post, user));

                    dto.setTotalLikes(this.calculeTotalLike(post.getId()));
                    dto.setTotalComments(this.calculeTotalComment(post.getId()));

                    return dto;
                })
                .toList();

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public Page<PostResponseDTO> searchPosts(Pageable pageable, String content) {
        UserDTO user = ExtractUser.get();
        Page<Posts> posts = this.postRepository.findByContentContainingCaseSensitiveAndActiveTrue(content, pageable);
        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    if (variables.getDeleteAfterOneDay()) {
                        this.verifyDateActive(post);
                    }

                    PostResponseDTO dto = new PostResponseDTO(post);

                    dto.setPostOwner(this.verifyPostOwner(user,post));
                    dto.setUserLike(this.verifyUserLike(post, user));

                    dto.setTotalLikes(this.calculeTotalLike(post.getId()));
                    dto.setTotalComments(this.calculeTotalComment(post.getId()));

                    return dto;
                })
                .toList();
        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public PostIDResponseDTO findByID(Long id) {
        UserDTO user = ExtractUser.get();

        Posts post = this.postRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Post with ID " + id + " not found."));

        PostIDResponseDTO postIDResponseDTO = new PostIDResponseDTO(post);
        postIDResponseDTO.setPostOwner(this.verifyPostOwner(user,post));
        postIDResponseDTO.setUserLike(this.verifyUserLike(post, user));

        postIDResponseDTO.setTotalLikes(this.calculeTotalLike(post.getId()));
        postIDResponseDTO.setTotalComments(this.calculeTotalComment(post.getId()));

        return postIDResponseDTO;
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

        PostIDResponseDTO postPersisted = this.findByID(id);

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


    private Long calculeTotalLike(Long postId) {

        Long totalLike = likeRepository.countByPostIdAndLikeEnum(postId, LikeEnum.LIKE);
        Long totalDislike = likeRepository.countByPostIdAndLikeEnum(postId, LikeEnum.DISLIKE);

        return totalLike - totalDislike;
    }

    private Long calculeTotalComment(Long postId) {

        return commentRepository.countByPostIdAndActiveIsTrue(postId);
    }

    private void verifyDateActive (Posts post) {

        Duration duration = Duration.between(post.getDate(), LocalDateTime.now());

        if (duration.toHours() >= 24) {
            post.setActive(Boolean.FALSE);
            postRepository.save(post);
        }
    }

    private LikeEnum verifyUserLike(Posts post, UserDTO user) {

        Likes like = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());

        if (like == null) {
            return null;
        }
        return like.getLikeEnum();
    }

    private Boolean verifyPostOwner(UserDTO user, Posts post) {

        return post.getUser().getEmail().equals(user.getEmail());
    }
}