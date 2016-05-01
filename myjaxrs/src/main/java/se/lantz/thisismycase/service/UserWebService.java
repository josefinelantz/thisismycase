package se.lantz.thisismycase.service;

import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class UserWebService
{
	private static final UserService userService = Loader.getBean(UserServiceImpl.class);
	private static final WorkItemService workItemService = Loader.getBean(WorkItemServiceImpl.class);

	@Context
	private UriInfo uriInfo;

	@GET
	public Response getAllUsers() {
		List<User> users = userService.findAllUsers();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users)
		{
		};
		return Response.ok(entity).build();
	}

	@POST
	public Response addUser(User user) {
		User storedUser = userService.saveOrUpdateUser(user);
		URI location = uriInfo.getAbsolutePathBuilder().path(storedUser.getId().toString()).build();
		return Response.created(location).build();
	}

	@GET
	@Path("{userId}")
	public Response getUser(@PathParam("userId") String userId) throws EntityNotFoundException {
		User user = userService.findByUserId(userId);
		return Response.ok(user).build();
	}

	@PUT
	@Path("{id}")
	public Response updateUser(@PathParam("id") Long id, User user) throws EntityNotFoundException {
		User updatedUser = new User(user.getUsername(), user.getFirstName(), user.getLastName());

		if (user.getUsername() == null || user.getUsername() == "") {
			return Response.status(400).entity("Provide username!").build();
		}

		if (user.getFirstName() == null || user.getFirstName() == "") {
			return Response.status(400).entity("Provide firstname!").build();
		}

		if (user.getLastName() == null || user.getLastName() == "") {
			return Response.status(400).entity("Provide lastname!").build();
		}

		updatedUser.setId(id);
		updatedUser.setUserId(user.getUserId());
    	updatedUser.setUsername(user.getUsername());
		updatedUser.setFirstName(user.getFirstName());
		updatedUser.setLastName(user.getLastName());
		userService.saveOrUpdateUser(updatedUser);
    	return Response.ok().entity(updatedUser).build();
	}

	@DELETE
	@Path("{userId}")
	public Response removeUser(@PathParam("userId") String userId) throws EntityNotFoundException {
		User removeUser = userService.findByUserId(userId);
		userService.removeUser(removeUser);
		return Response.noContent().build();
	}

	@GET
	@Path("search")
	public Response searchUsersByFirstNameOrLastNameOrUsername(@QueryParam("name") String name) throws Exception {
		List<User> users = userService.getUsersByFirstNameOrLastNameOrUsername(name);
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};

		return Response.ok(entity).build();
	}

	@GET
	@Path("{userId}/workitems")
	public Response getAllWorkItemsForUser(@PathParam("userId") String userId) throws Exception {
		List<WorkItem> workItems = workItemService.findWorkItemsByUser(userId);
		GenericEntity<List<WorkItem>> entity = new GenericEntity<List<WorkItem>>(workItems) {};
		return Response.ok(entity).build();
	}

	@GET
	@Path("team/{id}")
	public Response getAllWorkItemsForTeam(@PathParam("id") Long id) throws Exception {
		List<WorkItem> workItems = workItemService.findWorkItemsByTeam(id);
		GenericEntity<List<WorkItem>> entity = new GenericEntity<List<WorkItem>>(workItems)
		{
		};
		return Response.ok(entity).build();
	}
}
