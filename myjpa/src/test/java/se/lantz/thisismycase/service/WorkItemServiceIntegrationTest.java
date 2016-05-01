package se.lantz.thisismycase.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import se.lantz.thisismycase.config.*;
import se.lantz.thisismycase.model.Status;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

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
public class WorkItemServiceIntegrationTest {
    private WorkItem workItem;
    private WorkItem actualWorkItem;

    @Autowired
    private WorkItemService workItemService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldCreateWorkkItem() {
        workItem = new WorkItem("This is a workitem");
        actualWorkItem = workItemService.saveOrUpdate(workItem);

        assertNotNull(actualWorkItem.getId());
        assertThat(actualWorkItem.getDescription(), is("This is a workitem"));
    }

    @Test
    public void shouldChangeWorkItemStatus() {
        workItem = new WorkItem("description");
        workItemService.changeStatus("started", workItem);

        String status = workItem.getStatus();

        assertThat(status, is(Status.Started));
    }

    @Test
    public void shouldAssignWorkItemToUser() {
        User user = new User("sure", "baby", "yes");
        User createdUser = userService.saveOrUpdateUser(user);

        WorkItem workItem = new WorkItem("ok");
        WorkItem createdWorkItem = workItemService.saveOrUpdate(workItem);
        WorkItem assignedWorkItem = workItemService.assignWorkItemToUser(createdUser, createdWorkItem);

        assertThat(assignedWorkItem.getUser(), is(createdUser));
    }

    @Test
    public void shouldDeleteWorkItem() {
        workItem = new WorkItem("This one will be deleted!");
        WorkItem beforeWorkItem = workItemService.saveOrUpdate(workItem);

        workItemService.removeWorkItem(beforeWorkItem);
        WorkItem afterWorkItem = workItemService.findByWorkItemId(beforeWorkItem.getWorkItemId());

        assertNull(afterWorkItem);
    }


    @Test
    public void shouldFindWorkItemsByStatusOpen() {
        /*
        Initialized value when saving WorkItem is Status.Open.
         */
        workItem = new WorkItem("Try to find this workitem");
        WorkItem actualWorkItem = workItemService.saveOrUpdate(workItem);

        List<WorkItem> workItems = workItemService.findWorkItemsByStatus(Status.Open);

        assertThat(workItems.get(0).getStatus(), is(Status.Open));
    }

    @Test
    public void shouldFindWorkItemsByTeam() throws Exception {
        User user = new User("sure", "baby", "yes");
        User createdUser = userService.saveOrUpdateUser(user);

        WorkItem workItem = new WorkItem("ok");
        WorkItem createdWorkItem = workItemService.saveOrUpdate(workItem);
        workItemService.assignWorkItemToUser(createdUser, createdWorkItem);

        Team team = new Team("Happen");
        Team createdTeam = teamService.saveOrUpdateTeam(team);

        Team teamWithUser = teamService.addUserToTeam(createdTeam, createdUser);

        List<WorkItem> workItems = workItemService.findWorkItemsByTeam(teamWithUser.getId());

        assertThat(workItems.get(0).getUser().getFirstName(), is("baby"));
    }

    @Test
    public void shouldFindWorkItemsByUser() throws Exception {
        User user = new User("jola", "josefine", "lantz");
        workItem = new WorkItem("Try to find this workitem");
        User createdUser = userService.saveOrUpdateUser(user);
        WorkItem createdWorkItem = workItemService.assignWorkItemToUser(user, workItem);
        WorkItem assignedWorkItem = workItemService.assignWorkItemToUser(createdUser, createdWorkItem);

        List<WorkItem> workItems = workItemService.findWorkItemsByUser(createdUser.getUserId());

        assertThat(workItems.get(0).getUser().getFirstName(), is("josefine"));
        assertThat(workItems.get(0).getDescription(), is("Try to find this workitem"));
    }

    @Test
    public void shouldFindWorkItemBySearchString() throws Exception {
        workItem = new WorkItem("Try to find this workitem");
        WorkItem createdWorkItem = workItemService.saveOrUpdate(workItem);

        List<WorkItem> workItems = workItemService.findWorkItemsContaining("find");

        assertThat(workItems.get(0).getDescription(), is("Try to find this workitem"));
    }
}
