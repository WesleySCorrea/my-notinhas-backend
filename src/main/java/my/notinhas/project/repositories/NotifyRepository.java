package my.notinhas.project.repositories;

import my.notinhas.project.entities.Notify;
import my.notinhas.project.enums.ActionEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {

    Page<Notify> findAllByNotifyOwnerIdOrderByDateDesc(Long notifyOwner, Pageable pageable);

    void deleteByNotifyOwnerIdAndUserIdAndActionEnumAndPostId(Long notifyOwnerId, Long userId, ActionEnum action, Long postId);
    void deleteByCommentIdAndNotifyOwnerIdAndUserIdAndActionEnumAndPostId(Long commentId, Long notifyOwnerId, Long userId, ActionEnum action, Long postId);
    void deleteByCommentIdAndUserIdAndActionEnumAndPostId(Long commentId, Long userId, ActionEnum action, Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Notify n SET n.actionEnum = :newActionEnum WHERE n.notifyOwner.id = :notifyOwnerId " +
            "AND n.user.id = :userId AND n.post.id = :postId " +
            "AND n.actionEnum = :currentActionEnum")
    void updateActionEnumByNotifyOwnerIdAndUserIdAndPostId(@Param("newActionEnum") ActionEnum newActionEnum,
                                                                       @Param("notifyOwnerId") Long notifyOwnerId,
                                                                       @Param("userId") Long userId,
                                                                       @Param("postId") Long postId,
                                                                       @Param("currentActionEnum") ActionEnum currentActionEnum);

    @Modifying
    @Transactional
    @Query("UPDATE Notify n SET n.actionEnum = :newActionEnum WHERE n.notifyOwner.id = :notifyOwnerId " +
            "AND n.user.id = :userId AND n.post.id = :postId AND n.comment.id = :commentId " +
            "AND n.actionEnum = :currentActionEnum")
    void updateActionEnumByNotifyOwnerIdAndUserIdAndPostIdAndCommentId(@Param("newActionEnum") ActionEnum newActionEnum,
                                                           @Param("notifyOwnerId") Long notifyOwnerId,
                                                           @Param("userId") Long userId,
                                                           @Param("postId") Long postId,
                                                           @Param("commentId") Long commentId,
                                                           @Param("currentActionEnum") ActionEnum currentActionEnum);

    @Modifying
    @Transactional
    @Query("UPDATE Notify n SET n.verified = true WHERE n.id = :id AND n.notifyOwner.id = :notifyOwnerId")
    void updateVerifiedByIdAndNotifyOwnerId(@Param("id") Long id, @Param("notifyOwnerId") Long notifyOwnerId);
}