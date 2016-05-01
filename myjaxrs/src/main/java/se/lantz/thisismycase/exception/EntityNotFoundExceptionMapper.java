package se.lantz.thisismycase.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException>
{
	private final Gson gson = new Gson();
	
	@Override
	public Response toResponse(EntityNotFoundException exception)
	{
		return Response.status(Status.NOT_FOUND)
					   .entity(gson.toJson(exception.getMessage()))
					   .type(MediaType.APPLICATION_JSON)
					   .build();
	}
}
