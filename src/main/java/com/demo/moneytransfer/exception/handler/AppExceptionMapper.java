package com.demo.moneytransfer.exception.handler;

import com.demo.moneytransfer.exception.AppException;
import com.demo.moneytransfer.exception.ErrorMapper;
import com.demo.moneytransfer.exception.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    public Response toResponse(AppException ex) {
        int status = ErrorMapper.getStatusCode(ex.getCode());
        return Response.status(status)
                .entity(new ErrorMessage(ex))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }

}