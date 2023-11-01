package fr.univ.tln.tdomenge293;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
            log.info(e.getMessage());
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
        final ResourceConfig rc = new ResourceConfig().packages("fr.univ.tln.tdomenge293.rest","fr.univ.tln.tdomenge293.utils.rest");


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

}


