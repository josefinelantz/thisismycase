package se.lantz.thisismycase.repository;

import org.springframework.data.repository.CrudRepository;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long>
{
	User findByUserId(String userId);

	List<User> findAll();

	List<User> findByTeam(Team team);

	List<User> findAllByFirstNameOrLastNameOrUsername(String firstName, String lastName, String username);
}
