package my.notinhas.project.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.notinhas.project.entities.Comments;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.entities.Users;
import my.notinhas.project.enums.LikeEnum;
import my.notinhas.project.enums.ReactionEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserHistoryResponseDTO {
    private ReactionEnum reaction;
    private String content;
    private Long postId;
    private Long commentId;
    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
    private LocalDateTime date;

    public UserHistoryResponseDTO converterPostToUserHistory(Posts post) {

        UserHistoryResponseDTO userHistory = new UserHistoryResponseDTO();
        userHistory.setReaction(ReactionEnum.POST);
        userHistory.setContent(post.getContent());
        userHistory.adjustContent();
        userHistory.setPostId(post.getId());
        userHistory.setDate(post.getDate());

        return userHistory;
    }

    public UserHistoryResponseDTO converterCommentToUserHistory(Comments comment) {

        UserHistoryResponseDTO userHistory = new UserHistoryResponseDTO();
        userHistory.setReaction(ReactionEnum.COMMENT);
        userHistory.setContent(comment.getContent());
        userHistory.adjustContent();
        userHistory.setPostId(comment.getPost().getId());
        userHistory.setCommentId(comment.getId());
        userHistory.setDate(comment.getDate());

        return userHistory;
    }

    public UserHistoryResponseDTO converterReactionToUserHistory(Likes like) {

        UserHistoryResponseDTO userHistory = new UserHistoryResponseDTO();
        if (like.getLikeEnum() == LikeEnum.DISLIKE) {
            userHistory.setReaction(ReactionEnum.DISLIKE);
        } if (like.getLikeEnum() == LikeEnum.LIKE) {
            userHistory.setReaction(ReactionEnum.LIKE);
        }
        userHistory.setContent(like.getPost().getContent());
        userHistory.adjustContent();
        userHistory.setPostId(like.getPost().getId());
        userHistory.setDate(like.getDate());

        return userHistory;
    }

    public void adjustContent() {
        if (this.content != null && this.content.length() > 30) {
            this.content = this.content.substring(0, 27) + "...";
        }
    }
}