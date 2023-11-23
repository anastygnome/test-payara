package fr.univtln.tdomenge293.rest;


import fr.univtln.tdomenge293.model.jpa.Client;
import fr.univtln.tdomenge293.model.jpa.Commande;
import fr.univtln.tdomenge293.model.jpa.dao.OrderDAO;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
@ApplicationScoped
public class MyResource {
@Inject
OrderDAO dao;
    @PersistenceContext(unitName = "JpaPU")
    private EntityManager entityManager;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Client getIt() {
        return (Client) Client.of("test", "test", "test");


    }

    @Path("orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public Commande getItOrder() {
        Client clientJpa = (Client) Client.of("test", "test", "test@test.de");
        Commande orderJpa = Commande.of();
        orderJpa.setClient(clientJpa);
        entityManager.getTransaction().begin();
        entityManager.persist(orderJpa);
        entityManager.getTransaction().commit();
        entityManager.flush();
        return orderJpa;

    }
/*
    @Path("populate")
    @GET
    public boolean populate() {
        //ClientDAO daoClient = ClientDAO.of(Main.emf);
        OrderDAO daoOrder = OrderDAO.of(Main.emf);
        Faker faker = new Faker(Locale.FRENCH);
        for (int i = 0; i < 10000; i++) {
        Client client = Client.of(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress());
        OrderJpaImpl order = OrderJpaImpl.of(client);
        ItemJpaImpl item = ItemJpaImpl.of(faker.address().latLon(), BigDecimal.valueOf(faker.number().randomDouble(3,0,1000)));
        order.addLine(OrderLineJpaImpl.of(order,item,1));
        daoOrder.create(order);
        }
        return true;
    }
    */
}