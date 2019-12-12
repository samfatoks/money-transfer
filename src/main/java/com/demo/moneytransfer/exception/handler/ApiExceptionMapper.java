package com.demo.moneytransfer.exception.handler;

import com.demo.moneytransfer.exception.ApiException;
import com.demo.moneytransfer.exception.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    public Response toResponse(ApiException ex) {
        return Response.status(ex.getStatus())
                .entity(new ErrorMessage(ex))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }

}