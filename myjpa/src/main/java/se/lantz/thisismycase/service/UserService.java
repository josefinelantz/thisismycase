package se.lantz.thisismycase.service;

import se.lantz.thisismycase.model.User;
import java.util.List;

public interface UserService {

    User saveOrUpdateUser(User user);

    User findById(Long id);

    List<User> findAllUsers();

    User findByUserId(String userId);

    List<User> getUsersByFirstNameOrLastNameOrUsername(String name);

    List<User> findUsersByTeam(Long id);

    void removeUser(User user);
}
