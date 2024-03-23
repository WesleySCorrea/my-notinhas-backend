package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.dtos.UserDTO;
import my.notinhas.project.dtos.request.PostRequestDTO;
import my.notinhas.project.dtos.response.PostPublicResponseDTO;
import my.notinhas.project.dtos.response.PostResponseDTO;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.LikeEnum;
import my.notinhas.project.exception.runtime.ObjectNotFoundException;
import my.notinhas.project.exception.runtime.PersistFailedException;
import my.notinhas.project.exception.runtime.UnauthorizedIdTokenException;
import my.notinhas.project.repositories.LikeRepository;
import my.notinhas.project.repositories.PostRepository;
import my.notinhas.project.services.PostService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper mapper;

    @Override
    public Page<PostPublicResponseDTO> findAllPublic(Pageable pageable) {

        Page<Posts> posts = this.postRepository.findAllByActiveTrue(pageable);

        List<PostPublicResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    verifyDateActive(post);

                    return mapper.map(post, PostPublicResponseDTO.class);
                })
                .toList();

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public Page<PostResponseDTO> findAll(Pageable pageable) {

        Page<Posts> posts = this.postRepository.findAllByActiveTrue(pageable);

        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    verifyDateActive(post);

                    PostResponseDTO dto = mapper.map(post, PostResponseDTO.class);

                    dto.setTotalLikes(this.calculeTotalLike(post.getId()));

                    return dto;
                })
                .toList();

        return new PageImpl<>(postResponseDTO, pageable, posts.getTotalElements());
    }

    @Override
    public PostDTO findByID(Long id) {

        Posts post = this.postRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Post with ID " + id + " not found."));

        return mapper.map(post, PostDTO.class);
    }

    @Override
    public void savePost(PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO user = (UserDTO) authentication.getPrincipal();
        postRequestDTO.setUser(user);

        Posts request = new Posts();
        request.setDate(LocalDateTime.now());
        request.setContent(postRequestDTO.getContent());
        request.setActive(Boolean.TRUE);
        request.setUser(mapper.map(postRequestDTO.getUser(), Users.class));

        try {
            this.postRepository.save(request);
        } catch (Exception e) {
            throw new PersistFailedException("Fail when the object was persisted");
        }
    }

    @Override
    public void updatePost(PostRequestDTO postRequestDTO, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO user = (UserDTO) authentication.getPrincipal();
        postRequestDTO.setUser(user);

        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        var postPersisted = this.findByID(id);
        postPersisted.setContent(postRequestDTO.getContent());

        if (postPersisted.getUser().getUserName().equals(postRequestDTO.getUser().getUserName())) {
            this.postRepository.save(mapper.map(postPersisted, Posts.class));
        } else {
            throw new UnauthorizedIdTokenException("Post does not belong to the user");
        }
    }

    @Override
    public void deleteByID(Long id) {

        PostDTO post = this.findByID(id);

        if (post!=null) {
            this.postRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Post with ID: " + id + " not found!");
        }
    }

    private Long calculeTotalLike(Long postId) {

        Long totalLike = likeRepository.countByPostIdAndLikeEnum(postId, LikeEnum.LIKE);
        Long totalDislike = likeRepository.countByPostIdAndLikeEnum(postId, LikeEnum.DISLIKE);

        return totalLike - totalDislike;
    }

    private void verifyDateActive (Posts post) {

        Duration duration = Duration.between(post.getDate(), LocalDateTime.now());

        if (duration.toHours() >= 24) {
            post.setActive(Boolean.FALSE);
            postRepository.save(post);
        }
    }
}