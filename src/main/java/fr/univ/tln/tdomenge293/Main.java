package fr.univ.tln.tdomenge293;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http2.Http2AddOn;
import org.glassfish.grizzly.http2.Http2Configuration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

@Slf4j

/**
 * Main class.
 *
 */

public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final EntityManagerFactory emf;
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    static {
        EntityManagerFactory tmp = null;
        try {
            tmp = Persistence.createEntityManagerFactory("TpPU");
        } catch (Exception e) {
            log.warn("NO DB");
        }
        emf = tmp;
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in fr.univ.tln.tdomenge293.rest package
        final ResourceConfig rc = new ResourceConfig().packages("fr.univ.tln.tdomenge293.rest").register(CustomJacksonMapperProvider.class);


        Http2Configuration configuration = Http2Configuration.builder().build();
        Http2AddOn http2Addon = new Http2AddOn(configuration);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc, false);
        server.getListeners().stream().findFirst().ifPresent(listener -> listener.registerAddOn(http2Addon));

        try {
            server.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return server;
    }

    /**
     * Main method.
     *
     * @param args
     */

    public static void main(String[] args) {

        final HttpServer server = startServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Stopping server..");
            server.shutdownNow();
            if (emf != null && emf.isOpen()) emf.close();
        }, "shutdownHook"));

        log.info("Jersey app started with endpoints available at " + BASE_URI + " \nHit Ctrl-C to stop it...");

    }

    @Provider
    public static final class CustomJacksonMapperProvider implements ContextResolver<ObjectMapper> {

        final ObjectMapper mapper;

        public CustomJacksonMapperProvider() {
            mapper = new ObjectMapper().registerModule(new Hibernate6Module()).registerModule(new JavaTimeModule()).registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
                    // to allow implicit json construction without JsonProperty in constructor in some cases
                    .enable(SerializationFeature.INDENT_OUTPUT)             // enable pretty print (indent the JSON
                    .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return mapper;
        }
    }
}

