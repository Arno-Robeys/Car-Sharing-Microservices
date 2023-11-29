package be.ucll.userservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User validateUser(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }
}
