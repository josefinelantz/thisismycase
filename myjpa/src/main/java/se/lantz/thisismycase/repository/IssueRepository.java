package se.lantz.thisismycase.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.lantz.thisismycase.model.Issue;
import se.lantz.thisismycase.model.WorkItem;
import java.util.List;

public interface IssueRepository extends CrudRepository<Issue, Long>
{
    @Query("SELECT i.workItem FROM Issue i")
    List<WorkItem> findWorkItemsWithIssue();
}
