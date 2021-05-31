package SpringBoot.Policy_Module_Ultimate.services;

import SpringBoot.Policy_Module_Ultimate.dto.UserRegistrationDto;
import SpringBoot.Policy_Module_Ultimate.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByUsername(String username);
    User save(UserRegistrationDto registration);
}
