package se.lantz.thisismycase.model;

import javax.persistence.*;
import java.util.*;
/*
Fattade lite sent att Team hade passat bättre som @Embaddable, som
skulle ha en @ElementCollection av User i och med
att Team-information i princip aldrig är intressant för sig självt.
Labbade lite med detta, men jag fick inte till det utan att det skulle
behöva mer tid och just JPA är inte nummer 1 på min lista att bemästra.
 */
@Entity
public class Team extends AbstractEntity
{
	@Column(nullable = false)
	private String teamName;

	@Column(nullable = false)
	private String teamId;

	@OneToMany(mappedBy = "team", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private Collection<User> users;

	protected Team() {}

	public Team(final String teamName) {
		this.teamId = getEntityId();
		this.teamName = teamName;
		users = new ArrayList<>();
	}

	public Team addUserToTeam(final User user){
		if (!users.contains(user)) {
			user.setTeam(this);
			users.add(user);
		}
		return this;
	}

	public String getTeamName() {
		return teamName;
	}

	public List<User> getUsers() {
		return new ArrayList<>(users);
	}

	public void setTeamName(final String teamName) {
		this.teamName = teamName;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setUsers(final Collection<User> users) {
		this.users = users;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Team team = (Team) o;

		if (teamName != null ? !teamName.equals(team.teamName) : team.teamName != null) return false;
		if (teamId != null ? !teamId.equals(team.teamId) : team.teamId != null) return false;
		return users != null ? users.equals(team.users) : team.users == null;

	}

	@Override
	public int hashCode() {
		int result = teamName != null ? teamName.hashCode() : 0;
		result = 31 * result + (teamId != null ? teamId.hashCode() : 0);
		result = 31 * result + (users != null ? users.hashCode() : 0);
		return result;
	}
}
