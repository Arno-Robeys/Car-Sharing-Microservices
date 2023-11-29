package be.ucll.userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "`user`")
public class User {

    @Id
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
