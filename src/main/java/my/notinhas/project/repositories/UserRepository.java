package my.notinhas.project.repositories;

import my.notinhas.project.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByUserNameIgnoreCase(String userName);

    @Query("SELECT u FROM Users u WHERE u.userName ILIKE %:userName% AND u.active = true ORDER BY u.created DESC")
    Page<Users> findByUserNameContainingIgnoreCaseAndActiveTrue(@Param("userName")String userName, Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByUserNameIgnoreCase(String userName);
    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.active = false, u.userName = :userName, u.editatedBio = null, u.editatedUsername = null, u.bio = null WHERE u.id = :id")
    void updateUserFields(@Param("id") Long id, @Param("userName") String userName);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.refreshToken = :refreshToken, u.idToken = :token WHERE u.id = :id")
    void saveTokenAndRefreshToken(@Param("id") Long id, @Param("refreshToken") String refreshToken, @Param("token") String idToken);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.idToken = :token WHERE u.id = :id")
    void updateToken(@Param("id") Long id, @Param("token") String idToken);
    Users findByIdToken(String idToken);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.active = true WHERE u.email = :email")
    void activeUser(@Param("email") String email);

}
