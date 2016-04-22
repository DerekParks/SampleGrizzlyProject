package com.company.someWebService.util;

import org.glassfish.grizzly.utils.Exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Make web service return full stack track as text when encountering an error. This is useful for debugging but you make want to turn it off for production.
 */
public class ExToResponse implements
    ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception ex) {
        return Response.status(500).entity(Exceptions.getStackTraceAsString(ex)).type("text/plain")
                .build();
    }
}