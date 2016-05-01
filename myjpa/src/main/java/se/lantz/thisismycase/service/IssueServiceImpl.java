package se.lantz.thisismycase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lantz.thisismycase.model.Issue;
import se.lantz.thisismycase.model.Status;
import se.lantz.thisismycase.model.WorkItem;
import se.lantz.thisismycase.repository.IssueRepository;
import se.lantz.thisismycase.repository.WorkItemRepository;
import se.lantz.thisismycase.service.Utils.ToolBox;
import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IssueServiceImpl implements IssueService
{
	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private WorkItemRepository workItemRepository;

	public Issue saveOrUpdateIssue(final Issue issue) {
		return issueRepository.save(issue);
	}

	public Issue findById(Long id) {
		return issueRepository.findOne(id);
	}

	public Issue addIssueToWorkItem(final WorkItem workItem, final Issue issue) {
		if (ToolBox.checkStatus(workItem.getStatus()).equals(Status.Done)) {
			throw new IllegalArgumentException("An Issue can only be added to a WorkItem with WorkItemStatus.DONE");
		}

		Issue issueAddedToWorkItem = issue.setWorkItem(workItem);
		workItem.setStatusOpen();
		return saveOrUpdateIssue(issueAddedToWorkItem);
	}

	public Set<WorkItem> findWorkItemsWithIssues() throws Exception {
		List<WorkItem> issues = issueRepository.findWorkItemsWithIssue();
		if (issues.isEmpty()) {
			throw new EntityNotFoundException("No WorkItem with Issues was found");
		}
		return new HashSet<>(issues);
	}
}