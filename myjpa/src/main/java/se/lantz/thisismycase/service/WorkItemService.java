package se.lantz.thisismycase.service;

import org.hibernate.jdbc.Work;
import se.lantz.thisismycase.model.Status;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static se.lantz.thisismycase.model.Status.*;

public interface WorkItemService {

    WorkItem saveOrUpdate(WorkItem workItem);

    WorkItem changeStatus(String string, WorkItem workItem);

    WorkItem findByWorkItemId(String workItemId);

    WorkItem findById(Long id);

    WorkItem assignWorkItemToUser(User user, WorkItem workitem) throws IllegalArgumentException;

    List<WorkItem> findWorkItemsByStatus(String status);

    List<WorkItem> findWorkItemsByTeam(Long id) throws Exception;

    List<WorkItem> findWorkItemsByUser(String userId) throws Exception;

    List<WorkItem> findWorkItemsContaining(String searchString) throws Exception;

    void removeWorkItem(WorkItem workItem);
}
