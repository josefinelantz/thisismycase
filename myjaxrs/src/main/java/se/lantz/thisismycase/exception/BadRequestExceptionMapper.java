package se.lantz.thisismycase.exception;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException>
{
	private final Gson gson = new Gson();
	
	@Override
	public Response toResponse(BadRequestException exception)
	{
		return Response.status(Status.BAD_REQUEST)
					   .entity(gson.toJson(exception.getMessage()))
					   .type(MediaType.APPLICATION_JSON)
					   .build();
	}
}
