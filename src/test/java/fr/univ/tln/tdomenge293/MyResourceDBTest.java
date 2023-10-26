package fr.univ.tln.tdomenge293;

import fr.univ.tln.tdomenge293.Main;
import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.*;

@Slf4j
class MyResourceDBTest {

    private HttpServer server;
    private WebTarget target;
    private final Client client = ClientBuilder.newClient();

    @BeforeEach
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
            target = client.target(Main.BASE_URI);
        }

    @AfterEach
    public void tearDown() throws Exception {
        client.close();
        server.shutdownNow();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Tag("db")
    @Test
    void testGetIt() {
        ClientJpaImpl responseMsg = target.path("myresource").request().get(ClientJpaImpl.class);
        ClientJpaImpl test =  ClientJpaImpl.of("test","test","test");
        test.setNumber(responseMsg.getNumber());
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("TpPU");
             EntityManager em = emf.createEntityManager()
        ) {
            em.persist(responseMsg);
        }
        log.info(String.valueOf(test.hashCode()));
        log.info(String.valueOf(test.hashCode()));
        log.info(responseMsg.toString());
        Assertions.assertEquals(test,responseMsg);

    }
}
