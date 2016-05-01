package se.lantz.thisismycase.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class WorkItem extends AbstractEntity
{
	@Column (nullable = false)
	private String workItemId;

	@Column (nullable = false)
	private String description;

	@Column (nullable = false)
	private String status;

	@ManyToOne (cascade = CascadeType.MERGE)
	private User user;

	protected WorkItem() {}

	public WorkItem(String description) {
		this.workItemId = getEntityId();
		this.description = description;
		this.status = Status.Open;
	}

	public User getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public void setStatusOpen() {
		this.status = Status.Open;
	}

	public void setStatusClosed() {
		this.status = Status.Closed;
	}

	public WorkItem setUser(final User user) {
		this.user = user;
		return this;
	}

	public void setDescription(final String description) {
		this.description = description;

	}

	public void setStatusUnstarted(){
		this.status = Status.Unstarted;
	}

	public void setStatusStarted(){
		this.status = Status.Started;
	}

	public void setStatusDone(){
		this.status = Status.Done;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WorkItem workItem = (WorkItem) o;

		if (workItemId != null ? !workItemId.equals(workItem.workItemId) : workItem.workItemId != null) return false;
		if (description != null ? !description.equals(workItem.description) : workItem.description != null)
			return false;
		if (status != null ? !status.equals(workItem.status) : workItem.status != null) return false;
		return user != null ? user.equals(workItem.user) : workItem.user == null;

	}

	@Override
	public int hashCode() {
		int result = workItemId != null ? workItemId.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (user != null ? user.hashCode() : 0);
		return result;
	}
}
