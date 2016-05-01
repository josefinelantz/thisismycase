package se.lantz.thisismycase;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;
import se.lantz.thisismycase.service.*;


public final class Main {
	public static void main(String[] args) throws Exception {
		try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
			context.scan("se.lantz.thisismycase.config");
			context.refresh();

			UserService userService = context.getBean(UserServiceImpl.class);
			TeamService teamService = context.getBean(TeamServiceImpl.class);
			WorkItemService workItemService = context.getBean(WorkItemServiceImpl.class);
			//IssueServiceImpl issueService = context.getBean(IssueServiceImpl.class);

			User user = new User("ok", "baby", "yes");
			User createdUser = userService.saveOrUpdateUser(user);

			Team team = new Team("Happen");
            team.addUserToTeam(user);
			Team createdTeam = teamService.saveOrUpdateTeam(team);
			teamService.addUserToTeam(createdTeam, createdUser);

			WorkItem workItem = new WorkItem("ok");
			WorkItem createdWorkItem = workItemService.saveOrUpdate(workItem);
			workItemService.assignWorkItemToUser(createdUser, createdWorkItem);



			//WorkItem workitem = new WorkItem("Fix this bug now!");
			//workitem.setStatusDone();
			//workItemService.saveOrUpdate(workitem);
			//Issue issue = new Issue("what");
			//issueService.addIssueToWorkItem(workitem, issue);

			//workItemService.assignWorkItem(user, workitem);
			//
			//userService.findUserByAnyName("", "Jo").forEach(System.out::println);
		}
	}

}
