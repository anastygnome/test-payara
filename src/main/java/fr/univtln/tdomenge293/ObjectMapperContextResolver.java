package fr.univtln.tdomenge293;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        this.mapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper newMapper = new ObjectMapper();
        newMapper.enable(SerializationFeature.INDENT_OUTPUT);
        newMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        newMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        newMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        newMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return newMapper;
    }
}
