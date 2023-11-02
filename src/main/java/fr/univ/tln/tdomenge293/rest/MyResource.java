package fr.univ.tln.tdomenge293.rest;

import fr.univ.tln.tdomenge293.Main;
import fr.univ.tln.tdomenge293.dao.jpa.OrderDAO;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderJpaImpl;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ClientJpaImpl getIt() {
        ClientJpaImpl clientJpa = ClientJpaImpl.of("test", "test", "test");
        return clientJpa;


    }
    @Path("orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public OrderJpaImpl getItOrder() {
        ClientJpaImpl clientJpa = ClientJpaImpl.of("test", "test", "test@test.de");
        OrderJpaImpl orderJpa = OrderJpaImpl.of(BigDecimal.ZERO,clientJpa);
        OrderDAO dao = OrderDAO.of(Main.emf);
        dao.create(orderJpa);
        return orderJpa;

    }
}
