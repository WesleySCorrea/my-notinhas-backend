package my.notinhas.project.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
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
}
