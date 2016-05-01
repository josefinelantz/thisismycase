package se.lantz.thisismycase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.repository.TeamRepository;
import se.lantz.thisismycase.repository.UserRepository;
import se.lantz.thisismycase.repository.WorkItemRepository;
import se.lantz.thisismycase.service.Utils.ToolBox;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private WorkItemRepository workItemRepository;

	public User saveOrUpdateUser(final User user) {
		if (ToolBox.validUserName(user.getUsername())) {
			return userRepository.save(user);
		}
		throw new IllegalArgumentException("Username not valid, must have 10 chars.");
	}

	public User findById(final Long id) {
		return userRepository.findOne(id);
	}

	public List<User> findAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new EntityNotFoundException("No users yet.");
		}
		return users;
	}

	public User findByUserId(final String userId) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			throw new EntityNotFoundException("Doesn't exist.");
		}
		return user;
	}

	public void removeUser(final User user) {
		userRepository.delete(user.getId());
	}

	public List<User> getUsersByFirstNameOrLastNameOrUsername(final String name) {
		final String username = name;
		final String firstName = name;
		final String lastName = name;

		return userRepository.findAllByFirstNameOrLastNameOrUsername(firstName, lastName, username);
	}

	public List<User> findUsersByTeam(final Long id) {
		Team storedTeam = teamRepository.findOne(id);
		List<User> users = userRepository.findByTeam(storedTeam);
		if (users.isEmpty()) {
			throw new EntityNotFoundException(storedTeam.getTeamName() + "has not yet any users");
		}
		return users;
	}
}