package be.ucll.userservice.domain;

import org.springframework.stereotype.Component;

@Component
public class UserService {

    public User createUser(String username, String email, String password) {
        return new User(username, email, password);
    }
}
