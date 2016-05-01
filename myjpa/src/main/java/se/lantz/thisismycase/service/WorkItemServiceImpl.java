package se.lantz.thisismycase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;
import se.lantz.thisismycase.repository.IssueRepository;
import se.lantz.thisismycase.repository.TeamRepository;
import se.lantz.thisismycase.repository.UserRepository;
import se.lantz.thisismycase.repository.WorkItemRepository;
import se.lantz.thisismycase.service.Utils.ToolBox;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkItemServiceImpl implements WorkItemService
{
	@Autowired
	private WorkItemRepository workItemRepository;

	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	TeamRepository teamRepository;

	public WorkItem saveOrUpdate(final WorkItem workItem) {
		return workItemRepository.save(workItem);
	}

	public WorkItem assignWorkItemToUser(final User user, final WorkItem workItem) throws IllegalArgumentException {
		final User assignedUser = userRepository.save(user);

        List<WorkItem> items = workItemRepository.findByUser(assignedUser);
        if (ToolBox.userHasCapacity(items)) {
			WorkItem workItemToAssign = workItem.setUser(assignedUser);
			return saveOrUpdate(workItemToAssign);
        }
		throw new IllegalArgumentException("Only 5 allowed.");
    }

	public List<WorkItem> findWorkItemsByStatus(final String status) {
		if (ToolBox.checkStatus(status.toLowerCase()) != null) {
			List<WorkItem> workItems = workItemRepository.findByStatus(status);
			return workItems;
		}
		throw new EntityNotFoundException("No workitems with status: " + status);
	}

	public List<WorkItem> findWorkItemsByTeam(Long id) throws Exception {
		Team storedTeam = teamRepository.findOne(id);
		List<User> users = storedTeam.getUsers();
		if (users.isEmpty()) {
			throw new EntityNotFoundException("No workitems for this team yet.");
		}
		List<WorkItem> workItems = new ArrayList<>();
		users.forEach(user -> {
			workItems.addAll(workItemRepository.findByUser(user));
		});

		return workItems;
	}

	public List<WorkItem> findWorkItemsByUser(String userId) throws Exception {
		User storedUser = userRepository.findByUserId(userId);
		List<WorkItem> workItems = workItemRepository.findByUser(storedUser);
		if (workItems.isEmpty()) {
			throw new EntityNotFoundException("User doesn't have any workitems yet");
		}
		return workItems;
	}

	public List<WorkItem> findWorkItemsContaining(final String searchString) throws Exception {
		List<WorkItem> workItems = workItemRepository.findByDescriptionContainingIgnoreCase(searchString);
		if (workItems.isEmpty()) {
			throw new EntityNotFoundException("No results found on your search: " + searchString);
		}
		return workItems;
	}

	public WorkItem findById(final Long id) {
		return workItemRepository.findOne(id);
	}


	public WorkItem findByWorkItemId(final String workItemId) {
		return workItemRepository.findByWorkItemId(workItemId);
	}

	public void removeWorkItem(final WorkItem workItem) {
		workItemRepository.delete(workItem.getId());
	}

	public WorkItem changeStatus(final String status, final WorkItem workItem) {
		if(ToolBox.checkStatus(status.toLowerCase()) == null) {
			throw new IllegalArgumentException("Not a valid status.");
		}
		workItem.setStatus(status);
		return saveOrUpdate(workItem);
	}
}

