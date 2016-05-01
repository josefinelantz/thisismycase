package se.lantz.thisismycase.service;

import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import java.util.List;

public interface TeamService {

    Team saveOrUpdateTeam(Team team);

    Team findById(Long id);

    List<Team> findAllTeams();

    Team addUserToTeam(Team team, User user) throws Exception;

    void removeTeam(Team team);
}
