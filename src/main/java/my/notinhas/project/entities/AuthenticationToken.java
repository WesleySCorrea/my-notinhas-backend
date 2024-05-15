package my.notinhas.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "authentication_tokens")
public class AuthenticationToken {

    @Id
    @Column(name = "token", nullable = false, length = 50)
    private String token;

    @Column(name = "refresh_token", nullable = false, length = 300)
    private String refreshToken;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "picture")
    private String picture;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}
