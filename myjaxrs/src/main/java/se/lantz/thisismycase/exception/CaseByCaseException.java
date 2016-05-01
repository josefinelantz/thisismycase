package se.lantz.thisismycase.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CaseByCaseException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public CaseByCaseException(String message)
        {
            super(Response.status(Response.Status.NOT_ACCEPTABLE).entity(message).build());
        }
    }
