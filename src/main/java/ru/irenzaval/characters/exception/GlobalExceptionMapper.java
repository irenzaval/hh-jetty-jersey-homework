package ru.irenzaval.characters.exception;

import ru.irenzaval.characters.model.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {

        if (ex instanceof WebApplicationException webEx) {
            return Response.status(webEx.getResponse().getStatus())
                    .entity(new ErrorResponse(webEx.getMessage()))
                    .build();
        }

        ErrorResponse error = new ErrorResponse("Internal server error");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}
