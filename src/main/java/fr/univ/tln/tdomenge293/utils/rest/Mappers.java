package fr.univ.tln.tdomenge293.utils.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.stream.Collectors;
@SuppressWarnings("unused")

public final class Mappers {
    private Mappers() {
        throw new IllegalStateException("Shouldn't be instanced");
    }

    @Provider
    @SuppressWarnings("unused")
    @Singleton
    public static final class CustomJacksonMapperProvider implements ContextResolver<ObjectMapper> {

        final ObjectMapper mapper;

        public CustomJacksonMapperProvider() {
            mapper = new ObjectMapper().registerModule(new Hibernate6Module()).
                    registerModule(new JavaTimeModule())
                    .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
                    // to allow implicit json construction without JsonProperty in constructor in some cases
                    .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return mapper;
        }
    }

    @Singleton
    @Provider
    @SuppressWarnings("unused")
    public static final class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

        @Override
        public Response toResponse(ConstraintViolationException e) {
            String messages = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(",\n", "", "."));

            return Response.status(Response.Status.BAD_REQUEST).entity(messages).type(MediaType.TEXT_PLAIN_TYPE).build();
        }

    }

    @Provider
    public static final class ErrorHandler implements ExceptionMapper<Throwable> {

        @Override
        public Response toResponse(Throwable e) {

            Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            responseBuilder.entity(e.getMessage());

            return responseBuilder.build();
        }
    }
}