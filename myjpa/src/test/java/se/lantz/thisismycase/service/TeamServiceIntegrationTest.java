package se.lantz.thisismycase.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import se.lantz.thisismycase.config.AppConfigTest;
import se.lantz.thisismycase.config.ServiceConfig;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;

import javax.persistence.EntityNotFoundException;

import static org.hamcrest.core.Is.is;
import java.util.List;

import static org.junit.Assert.*;
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@ContextConfiguration(classes = { AppConfigTest.class, ServiceConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class TeamServiceIntegrationTest {

    private Team team;
    private Team actualTeam;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldCreateTeam() {
        team = new Team("testTeamName");
        teamService.saveOrUpdateTeam(team);

        actualTeam = teamService.saveOrUpdateTeam(team);

        assertNotNull(actualTeam.getId());
        assertThat(actualTeam.getTeamName(), is(team.getTeamName()));
    }

    @Test
    public void shouldUpdateTeam() {
        team = new Team("testTeamName");
        Team oldTeam = teamService.saveOrUpdateTeam(team);
        oldTeam.setTeamName("newTeamName");
        actualTeam = teamService.saveOrUpdateTeam(oldTeam);

        assertThat(actualTeam.getTeamName(), is("newTeamName"));
    }

    /*@Test
    public void shouldFindAllTeams() throws Exception {
        User one = new User("firstUsername", "FirstFirstName", "FirstLastName");
        User two = new User("secondUsername", "SecondFirstName", "SecondLastName");
        User createdOne = userService.saveOrUpdateUser(one);
        User createdTwo = userService.saveOrUpdateUser(two);
        team = new Team("teamWithTwoUsers");
        Team createdTeam = teamService.saveOrUpdateTeam(team);

        actualTeam = teamService.addUserToTeam(createdTeam, createdOne);
        actualTeam = teamService.addUserToTeam(createdTeam, createdTwo);

        List<Team> teams = teamService.findAllTeams();

        assertThat(teams.get(0).getUsers().size(), is(2));
    }*/

    @Test
    public void shouldAddUserToTeam() throws Exception {
        User user = new User("jola", "josefine", "lantz");
        User savedUser = userService.saveOrUpdateUser(user);

        team = new Team("teamWithOneUser");
        Team savedTeam = teamService.saveOrUpdateTeam(team);

        Team teamWithUsers = teamService.addUserToTeam(savedTeam, savedUser);

        assertThat(teamWithUsers.getUsers().get(0).getFirstName(), is("josefine"));
    }

    @Test (expected = EntityNotFoundException.class)
    public void shouldDeleteTeam() throws Exception {
        team = new Team("removed");
        Team beforeTeam = teamService.saveOrUpdateTeam(team);
        teamService.removeTeam(beforeTeam);
        Team afterTeam = teamService.findById(beforeTeam.getId());
    }
}