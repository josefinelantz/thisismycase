package se.lantz.thisismycase.service;

import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/workitem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class WorkItemWebService
{
    private static final WorkItemService workItemService = Loader.getBean(WorkItemService.class);
    private static final UserService userService = Loader.getBean(UserService.class);
    private static final TeamService teamService = Loader.getBean(TeamService.class);

    @Context
    private UriInfo uriInfo;

    @POST
    public Response createWorkItem(final WorkItem workItem) throws Exception {
        WorkItem createdWorkItem = workItemService.saveOrUpdate(workItem);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdWorkItem.getId().toString()).build();
        return Response.created(location).build();
    }

    @PUT
    @Path("{id}/{status}")
    public Response changeWorkItemStatus(@PathParam("id") final Long id, @PathParam("status") final String status) {
        WorkItem workItem = workItemService.findById(id);
        workItemService.changeStatus(status, workItem);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteWorkItem(@PathParam("id") final Long id) {
        WorkItem workItem = workItemService.findById(id);
        workItemService.removeWorkItem(workItem);
        return Response.noContent().build();
    }

    @PUT
    @Path("{id}/user/{userId}")
    public Response assignWorkItemToUser(@PathParam("id") final Long id, @PathParam("userId") final String userId) {
        User user = userService.findByUserId(userId);
        WorkItem workItem = workItemService.findById(id);
        workItemService.assignWorkItemToUser(user, workItem);
        return Response.noContent().build();
    }

    @GET
    @Path("{status}")
    public Response getWorkItemsByStatus(@PathParam("status") final String status) {
        List<WorkItem> workItems = workItemService.findWorkItemsByStatus(status);
        GenericEntity<List<WorkItem>> entity = new GenericEntity<List<WorkItem>>(workItems) {};
        return Response.ok(entity).build();
    }

    @GET
    @Path("search")
    public Response searchWorkItemsByDescriptionContaining(@QueryParam("value") final String value) throws Exception {
        if (value.length() == 0) {
            return Response.status(400).entity("Write something!").build();
        }
        List<WorkItem> workItems = workItemService.findWorkItemsContaining(value);
        GenericEntity<List<WorkItem>> entity = new GenericEntity<List<WorkItem>>(workItems){};
        return Response.ok(entity).build();
    }
}
