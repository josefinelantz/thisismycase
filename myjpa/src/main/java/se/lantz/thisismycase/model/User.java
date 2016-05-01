package se.lantz.thisismycase.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class User extends AbstractEntity
{
	@Column
	private String userId;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Team team;

	protected User() {}

	public User(final String username, final String firstName, final String lastName)
	{
		this.userId = getEntityId();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getUsername()
	{
		return username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public Team getTeam()
	{
		return team;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public User setTeam(final Team team) {
		if (this.team == null || !this.team.equals(team)) {
			this.team = team;
			team.addUserToTeam(this);
		}
		else if (this.team != null && !this.team.equals(team)){
			this.team.getUsers().remove(this);
			this.team = team;
		}
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
		if (username != null ? !username.equals(user.username) : user.username != null) return false;
		if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
		if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
		return team != null ? team.equals(user.team) : user.team == null;

	}

	@Override
	public int hashCode() {
		int result = userId != null ? userId.hashCode() : 0;
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (team != null ? team.hashCode() : 0);
		return result;
	}
}
