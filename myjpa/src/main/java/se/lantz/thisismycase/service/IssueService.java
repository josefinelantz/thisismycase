package se.lantz.thisismycase.service;

import se.lantz.thisismycase.model.Issue;
import se.lantz.thisismycase.model.WorkItem;
import java.util.Set;

public interface IssueService {

    Issue saveOrUpdateIssue(Issue issue);

    Issue findById(Long id);

    Issue addIssueToWorkItem(WorkItem workItem, Issue issue);

    Set<WorkItem> findWorkItemsWithIssues() throws Exception;
}
