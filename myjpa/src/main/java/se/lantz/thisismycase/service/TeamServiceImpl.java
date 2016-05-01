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
public class TeamServiceImpl implements TeamService
{
	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkItemRepository workItemRepository;

	public Team saveOrUpdateTeam(final Team team) {
		return teamRepository.save(team);
	}

	public List<Team> findAllTeams() {
		List<Team> teams = teamRepository.findAllTeams();
		if (teams.isEmpty()) {
			throw new EntityNotFoundException("No Teams were found");
		}
		return teams;
	}

	public Team findById(final Long id) {
		Team team = teamRepository.findOne(id);
		if (team == null) {
			throw new EntityNotFoundException("No such team exist");
		}
		return team;
	}

	public Team addUserToTeam(final Team team, final User user) throws IllegalArgumentException {
		if (ToolBox.okToAddUser(team, user)) {
			Team teamUserAdded = team.addUserToTeam(user);
			return saveOrUpdateTeam(teamUserAdded);
		}
		throw new IllegalArgumentException("User was not added, already member of other team or team full.");
	}

	public void removeTeam(final Team team) {
		teamRepository.delete(team.getId());
	}
}
