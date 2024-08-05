package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Users {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "google_id", unique = true)
    private String googleId;
    @Column(name = "user_name", unique = true)
    private String userName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "picture")
    private String picture;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "edited_username")
    private LocalDateTime editatedUsername;
    @Column(name = "bio")
    private String bio;
    @Column(name = "editated_bio")
    private LocalDateTime editatedBio;
    @Column(name = "active")
    private Boolean active;

    //Auth
    @Column(name = "refresh_token", length = 300)
    private String refreshToken;
    @Column(name = "id_token", length = 50)
    private String idToken;


    public Users(Long id, String googleId, String userName, String email, String firstName, String lastName, String picture, LocalDateTime created, LocalDateTime editatedUsername, String bio, LocalDateTime editatedBio, Boolean active) {
        this.id = id;
        this.googleId = googleId;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.created = created;
        this.editatedUsername = editatedUsername;
        this.bio = bio;
        this.editatedBio = editatedBio;
        this.active = active;
    }


}
