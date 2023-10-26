package fr.univ.tln.tdomenge293;

import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class MyResourceTest {

    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        target = ClientBuilder.newClient().target(Main.BASE_URI);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.shutdownNow();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    void testGetIt() {
        ClientJpaImpl responseMsg = target.path("myresource").request().get(ClientJpaImpl.class);
        ClientJpaImpl test = ClientJpaImpl.of("test", "test", "test");
        test.setNumber(responseMsg.getNumber());
        log.info(String.valueOf(test.hashCode()));
        log.info(String.valueOf(test.hashCode()));
        log.info(responseMsg.toString());
        Assertions.assertEquals(test, responseMsg);

    }
}
