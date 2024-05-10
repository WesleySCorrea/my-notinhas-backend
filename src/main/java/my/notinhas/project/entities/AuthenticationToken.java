package my.notinhas.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


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
}
