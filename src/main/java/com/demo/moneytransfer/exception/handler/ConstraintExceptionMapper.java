package com.demo.moneytransfer.exception.handler;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;

@Provider
public class ConstraintExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        JsonObject errorObj = new JsonObject();
        errorObj.addProperty("message", "Validation Error");
        errorObj.add("errors", prepareMessage(exception));
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorObj.toString())
                .type("application/json")
                .build();
    }

    private JsonArray prepareMessage(ConstraintViolationException exception) {
        JsonArray msgArr = new JsonArray();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            msgArr.add(String.format("%s %s", cv.getPropertyPath(), cv.getMessage()));
        }

        return msgArr;
    }
}