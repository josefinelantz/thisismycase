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
import se.lantz.thisismycase.model.*;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@ContextConfiguration(classes = { AppConfigTest.class, ServiceConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class IssueServiceTest {

    private Issue issue;
    private Issue actualIssue;
    private WorkItem workItem;
    private WorkItem actualWorkItem;

    @Autowired
    private IssueService issueService;

    @Autowired
    private WorkItemService workItemService;

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void shouldCreateIssue() throws Exception {
        issue = new Issue("A new issue");

        actualIssue = issueService.saveOrUpdateIssue(issue);

        assertNotNull(actualIssue.getId());
        assertThat(actualIssue.getIssueDescription(), is("A new issue"));
    }

    @Test
    public void shouldAddIssueToWorkItem() throws Exception {
        workItem = new WorkItem("A workitem");
        issue = new Issue("An issue");

        actualIssue = issueService.saveOrUpdateIssue(issue);
        actualWorkItem = workItemService.saveOrUpdate(workItem);

        Issue addedIssue = issueService.addIssueToWorkItem(actualWorkItem, actualIssue);

        assertThat(addedIssue.getIssueDescription(), is("An issue"));
    }

    @Test
    public void shouldFindWorkItemsWithIssues() throws Exception {
        workItem = new WorkItem("A workitem");
        issue = new Issue("An issue");

        actualIssue = issueService.saveOrUpdateIssue(issue);
        actualWorkItem = workItemService.saveOrUpdate(workItem);

        Issue addedIssue = issueService.addIssueToWorkItem(actualWorkItem, actualIssue);

        Set<WorkItem> workItemsWithIssues = issueService.findWorkItemsWithIssues();

        assertTrue(workItemsWithIssues.contains(actualWorkItem));
    }

    @Test
    public void testRemoveIssueById() throws Exception {

    }
}
