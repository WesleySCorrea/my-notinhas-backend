package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
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
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;

    @Override
    public Page<PostPublicResponseDTO> findAllPublic(Pageable pageable) {

        Page<Posts> posts = this.postRepository
                .findAllByActiveTrueOrderByDateDesc(pageable);

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
        UserDTO user = ExtractUser.get();

        Page<Posts> posts = this.postRepository
                .findAllByActiveTrueOrderByDateDesc(pageable);

        List<PostResponseDTO> postResponseDTO = posts.stream()
                .map(post -> {
                    this.verifyDateActive(post);

                    PostResponseDTO dto = mapper.map(post, PostResponseDTO.class);

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
                    this.verifyDateActive(post);

                    PostResponseDTO dto = mapper.map(post, PostResponseDTO.class);

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

        PostIDResponseDTO postIDResponseDTO = mapper.map(post, PostIDResponseDTO.class);
        postIDResponseDTO.setPostOwner(this.verifyPostOwner(user,post));
        postIDResponseDTO.setUserLike(this.verifyUserLike(post, user));

        postIDResponseDTO.setTotalLikes(this.calculeTotalLike(post.getId()));
        postIDResponseDTO.setTotalComments(this.calculeTotalComment(post.getId()));

//        List<Comments> comments = commentRepository.findByPostIdAndParentCommentIsNull(post.getId());
//
//        List<CommentResponseDTO> commentResponseDTOs = comments.stream()
//                .map(comment -> new CommentResponseDTO()
//                        .converterCommentToCommentResponse(comment, user))
//                .toList();
//
//
//        postIDResponseDTO.setComments(commentResponseDTOs);
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

        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        var postPersisted = this.findByID(id);
        postPersisted.setContent(postRequestDTO.getContent());


        if (postPersisted.getUser().getUserName().equals(user.getUserName())) {
            Posts postsToPersistd = mapper.map(postPersisted, Posts.class);
            postsToPersistd.setUser(user.convertUserDTOToUser());
            postsToPersistd.setIsEdited(Boolean.TRUE);
            postsToPersistd.setActive(Boolean.TRUE);

            this.postRepository.save(postsToPersistd);
        } else {
            throw new UnauthorizedIdTokenException("Post does not belong to the user");
        }
    }

    @Override
    public void deleteByID(Long id) {
        var user = ExtractUser.get();
        var post = this.postRepository.findById(id);
        if (post.isPresent() && post.get().getUser().getEmail().equals(user.getEmail())) {
            var entity = post.get();
                entity.setActive(Boolean.FALSE);
                this.postRepository.save(entity);
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