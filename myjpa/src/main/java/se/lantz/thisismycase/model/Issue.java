package se.lantz.thisismycase.model;

import javax.persistence.*;

@Entity
public class Issue extends AbstractEntity
{
	@ManyToOne (cascade = CascadeType.MERGE)
	private WorkItem workItem;

	@Column (nullable = false)
	private String issueId;

	@Column (nullable = false)
	private String issueDescription;

	protected Issue() {}

	public Issue(String issueDescription) {
		this.issueId = getEntityId();
		this.issueDescription = issueDescription;
	}

	public String getIssueDescription()
	{
		return issueDescription;
	}

	public WorkItem getWorkItem()
	{
		return workItem;
	}

	public void setDescription(final String description)
	{
		this.issueDescription = description;
	}

	public Issue setWorkItem(WorkItem workItem) {
		this.workItem = workItem;
		return this;
	}

	public String getIssueId() {
		return issueId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Issue issue = (Issue) o;

		if (workItem != null ? !workItem.equals(issue.workItem) : issue.workItem != null) return false;
		if (issueId != null ? !issueId.equals(issue.issueId) : issue.issueId != null) return false;
		return issueDescription != null ? issueDescription.equals(issue.issueDescription) : issue.issueDescription == null;

	}

	@Override
	public int hashCode() {
		int result = workItem != null ? workItem.hashCode() : 0;
		result = 31 * result + (issueId != null ? issueId.hashCode() : 0);
		result = 31 * result + (issueDescription != null ? issueDescription.hashCode() : 0);
		return result;
	}
}
