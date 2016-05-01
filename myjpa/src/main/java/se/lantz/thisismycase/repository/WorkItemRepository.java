package se.lantz.thisismycase.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

import java.util.List;

public interface WorkItemRepository extends CrudRepository<WorkItem, Long>
{
	WorkItem findByWorkItemId(String id);

	List<WorkItem> findByStatus(String status);

	List<WorkItem> findByDescriptionContainingIgnoreCase(String searchString);

	List<WorkItem> findByUser(User user);
}
