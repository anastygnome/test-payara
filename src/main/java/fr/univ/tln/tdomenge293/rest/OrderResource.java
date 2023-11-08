package fr.univ.tln.tdomenge293.rest;

import fr.univ.tln.tdomenge293.Main;
import fr.univ.tln.tdomenge293.jpa.dao.OrderDAO;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("order")
@Slf4j
public class OrderResource {
    private static final OrderDAO dao = OrderDAO.of(Main.emf);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderJpaImpl> getIt() {
        try (
            EntityManager em = Main.emf.createEntityManager())
        {
            return em.createQuery("select c from  OrderJpaImpl c ", OrderJpaImpl.class).getResultList();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrderJpaImpl getOrder(@PathParam("id") UUID id) {
        OrderJpaImpl result = dao.findById(id);
        if (result == null) throw new NotFoundException();
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveOrder(@Context UriInfo uriInfo, OrderJpaImpl r) {
        URI newResourceUri;
        if (r == null || r.getNumber() != null) throw new BadRequestException();
        log.info(r.toString());
        try {
            dao.create(r);
            newResourceUri = uriInfo.getAbsolutePathBuilder().path(r.getNumber().toString()).build();
            //dao.delete(r);
            return Response.created(newResourceUri).entity(r).build();
        } catch (RollbackException e) {
            log.error(e.getClass().getName());
            return Response.status(Response.Status.CONFLICT).build();

        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteOrder(@PathParam("id") UUID id) {
        try {
            dao.deleteByID(id);
            return Response.noContent().build();
        } catch (PersistenceException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response updateOrder(OrderJpaImpl order, @PathParam("id") UUID id) {
        if (order == null || order.getNumber() != null && order.getNumber() != id) throw new BadRequestException();
        order.setNumber(id);
        try {
            dao.update(order);

        }
        catch (RollbackException e) {
            if (e.getCause() instanceof ConstraintViolationException){
                return Response.status(Response.Status.CONFLICT).build();
            }
            else return Response.serverError().build();
        }
        return Response.noContent().build();

    }
}
