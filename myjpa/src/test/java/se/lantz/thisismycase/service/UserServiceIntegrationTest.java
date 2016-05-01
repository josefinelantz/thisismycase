package se.lantz.thisismycase.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@ContextConfiguration(classes = { AppConfigTest.class, ServiceConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceIntegrationTest {
    private User user;
    private User actualUser;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Test
    public void shouldCreateUser() {
        User newUser = new User("jola", "josefine", "lantz");
        actualUser = userService.saveOrUpdateUser(newUser);

        assertNotNull(actualUser.getId());
        assertThat(actualUser.getUsername(), is("jola"));
        assertThat(actualUser.getFirstName(), is("josefine"));
        assertThat(actualUser.getLastName(), is("lantz"));
    }

    @Test
    public void shouldGetUserByUserId() {
        User user = new User("jola", "jo", "lantz");

        User createdUser = userService.saveOrUpdateUser(user);

        actualUser = userService.findByUserId(createdUser.getUserId());

        assertThat(actualUser, is(createdUser));
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User("jola", "josefine", "lantz");
        User createdUser = userService.saveOrUpdateUser(user);

        createdUser.setUsername("wait");
        createdUser.setFirstName("Think");
        createdUser.setLastName("Breath");

        User updatedUser = userService.saveOrUpdateUser(createdUser);

        assertThat(updatedUser.getId(), is(createdUser.getId()));
        assertThat(updatedUser.getUsername(), is("wait"));
        assertThat(updatedUser.getFirstName(), is("Think"));
        assertThat(updatedUser.getLastName(), is("Breath"));
    }

    @Test
    public void shouldDeleteUserByUserIdShouldThrowEntityNotFoundException() {
        User user = new User("jola", "josefine", "lantz");
        User createdUser = userService.saveOrUpdateUser(user);
        userService.removeUser(createdUser);

        User deleted = userService.findById(createdUser.getId());

        assertNull(deleted);
    }

    @Test
    public void shouldGetUserByAnyName() {
        User user = new User("testUsername", "testFirstName", "testLastName");
        User createdUser = userService.saveOrUpdateUser(user);
        List<User> users = userService.getUsersByFirstNameOrLastNameOrUsername("testUsername");

        assertThat(users.get(0), is(createdUser));
        assertThat(users.get(0).getUsername(), is("testUsername"));
    }

    @Test
    public void shouldGetUserByTeam() throws Exception {
        Team team = new Team("Name");
        User user = new User("Username", "FirstName", "LastName");
        User createdUser = userService.saveOrUpdateUser(user);
        Team createdTeam = teamService.saveOrUpdateTeam(team);
        Team addedTeam = teamService.addUserToTeam(createdTeam, createdUser);
        List<User> users = userService.findUsersByTeam(createdTeam.getId());

        assertThat(users.get(0).getTeam().getTeamName(), is("Name"));
    }
}
