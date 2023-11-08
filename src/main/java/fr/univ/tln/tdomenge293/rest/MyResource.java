package fr.univ.tln.tdomenge293.rest;

import fr.univ.tln.tdomenge293.Main;
import fr.univ.tln.tdomenge293.jpa.dao.OrderDAO;
import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.ItemJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderLineJpaImpl;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.Locale;

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
        OrderJpaImpl orderJpa = OrderJpaImpl.of(clientJpa);
        OrderDAO dao = OrderDAO.of(Main.emf);
        dao.create(orderJpa);
        return orderJpa;

    }

    @Path("populate")
    @GET
    public boolean populate() {
        //ClientDAO daoClient = ClientDAO.of(Main.emf);
        OrderDAO daoOrder = OrderDAO.of(Main.emf);
        Faker faker = new Faker(Locale.FRENCH);
        for (int i = 0; i < 10000; i++) {
        ClientJpaImpl client = ClientJpaImpl.of(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress());
        OrderJpaImpl order = OrderJpaImpl.of(client);
        ItemJpaImpl item = ItemJpaImpl.of(faker.address().latLon(), BigDecimal.valueOf(faker.number().randomDouble(3,0,1000)));
        order.addLine(OrderLineJpaImpl.of(order,item,1));
        daoOrder.create(order);
        }
        return true;
    }
}