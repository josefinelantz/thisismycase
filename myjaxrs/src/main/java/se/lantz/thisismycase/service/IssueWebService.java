package se.lantz.thisismycase.service;

import se.lantz.thisismycase.model.Issue;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Set;

@Path("/issue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class IssueWebService
{
    private static final IssueService issueService = Loader.getBean(IssueService.class);
    private static final WorkItemService workItemService = Loader.getBean(WorkItemService.class);

    @Context
    private UriInfo uriInfo;

    @POST
    public Response createIssue(final Issue issue) throws Exception {
        Issue createdIssue = issueService.saveOrUpdateIssue(issue);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdIssue.getId().toString()).build();
        return Response.created(location).build();
    }

    @GET
    @Path("{id}")
    public Response getIssue(@PathParam("id") final Long id) throws Exception {
        Issue issue = issueService.findById(id);
        return Response.ok(issue).build();
    }

    @PUT
    @Path("{id}")
    public Response updateIssue(@PathParam("id") final Long id, final Issue issue) throws Exception {
        Issue updatedIssue = new Issue(issue.getIssueDescription());
        if (issue.getIssueDescription() == null || issue.getIssueDescription() == "") {
            return Response.status(400).entity("Provide a description!").build();
        }

        updatedIssue.setId(id);
        updatedIssue.setWorkItem(issue.getWorkItem());
        issueService.saveOrUpdateIssue(updatedIssue);
        return Response.ok().entity(updatedIssue).build();
    }

    @PUT
    @Path("{id}{workItemId}")
    public Response addIssueToWorkItem(@PathParam("id") final Long id, @PathParam("workItemId") final String workItemId)
            throws Exception {
        Issue issue = issueService.findById(id);
        WorkItem workItem = workItemService.findByWorkItemId(workItemId);
        issueService.addIssueToWorkItem(workItem, issue);
        return Response.noContent().build();
    }

    @GET
    @Path("bugs")
    public Response getWorkItemsWithIssue() throws Exception {
        Set<WorkItem> workItems = issueService.findWorkItemsWithIssues();
        GenericEntity<Set<WorkItem>> entity = new GenericEntity<Set<WorkItem>>(workItems){};
        return Response.ok(entity).build();
    }
}
