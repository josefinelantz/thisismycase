package se.lantz.thisismycase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("team")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class TeamWebService
{
    private static final TeamService teamService = Loader.getBean(TeamServiceImpl.class);
    private static final UserService userService = Loader.getBean(UserServiceImpl.class);
    private static final WorkItemService workItemService = Loader.getBean(WorkItemServiceImpl.class);

    @Context
    private UriInfo uriInfo;

    @POST
    public Response addTeam(Team team) {
        Team storedTeam = teamService.saveOrUpdateTeam(team);
        final URI location = uriInfo.getAbsolutePathBuilder().path(storedTeam.getId().toString()).build();
        return Response.created(location).build();
    }

    @GET
    public Response getAllTeams() {
        List<Team> teams = teamService.findAllTeams();
        GenericEntity<List<Team>> entity = new GenericEntity<List<Team>>(teams) {};
        return Response.ok(entity).build();
    }

    @GET
    @Path("{id}")
    public Response getTeam(@PathParam("id") Long id) {
        Team team = teamService.findById(id);
        return Response.ok(team).build();

    }

    @PUT
    @Path("{id}")
    public Response updateTeam(@PathParam("id") Long id, Team team) {
        Team updatedTeam = new Team(team.getTeamName());
        if (team.getTeamName() == null) {
            return Response.status(400).entity("Provide Team name!").build();
        }
        updatedTeam.setId(id);
        updatedTeam.setTeamId(team.getTeamId());
        updatedTeam.setUsers(team.getUsers());
        teamService.saveOrUpdateTeam(updatedTeam);
        return Response.ok().entity(updatedTeam).build();
    }


    @GET
    @Path("{id}/workitems")
    public Response findAllWorkItemsForTeam(@PathParam("id") Long id) throws Exception {
        List<WorkItem> workItems = workItemService.findWorkItemsByTeam(id);
        GenericEntity<List<WorkItem>> entity = new GenericEntity<List<WorkItem>>(workItems) {};
        return Response.ok(entity).build();
    }

    @PUT
    @Path("{id}/users")
    public Response addUserToTeam(@PathParam("id") Long id, String userId) throws Exception {
        Team team = teamService.findById(id);
        User user = userService.findByUserId(userId);
        Team updatedTeam = teamService.addUserToTeam(team, user);
        return Response.ok().entity(updatedTeam).build();
    }

    @DELETE
    @Path("{id}")
    public Response removeTeam(@PathParam("id") Long id) {
        Team team = teamService.findById(id);
        teamService.removeTeam(team);
        return Response.noContent().build();
    }
}

