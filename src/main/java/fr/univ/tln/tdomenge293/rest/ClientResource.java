package fr.univ.tln.tdomenge293.rest;

import fr.univ.tln.tdomenge293.jpa.dao.ClientDAO;
import fr.univ.tln.tdomenge293.jpa.dto.ClientDTOMapper;
import fr.univ.tln.tdomenge293.jpa.dto.ClientJpaDto;
import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static fr.univ.tln.tdomenge293.Main.emf;

@Slf4j
@Path("clients")

public class ClientResource {
    private final ClientDAO dao = ClientDAO.of(emf);
    @GET
@Produces(MediaType.APPLICATION_JSON)
    public List<ClientJpaImpl> getIt() {
       return dao.findAll();
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ClientJpaDto getclient(@PathParam("id") UUID id) {
        ClientJpaDto result = ClientDTOMapper.getDto(id);
        if (result == null) throw new NotFoundException();
        return result;
    }
    @GET
    @Path("{id}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderJpaImpl> getOrders(@PathParam("id") UUID id) {
        try (EntityManager em = emf.createEntityManager()){
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderJpaImpl> query = cb.createQuery(OrderJpaImpl.class);
            Root<OrderJpaImpl> order = query.from(OrderJpaImpl.class);
            Join<OrderJpaImpl, ClientJpaImpl> client = order.join("client", JoinType.INNER);
            query.select(order).where(cb.equal(client.get("id"), id));

            return em.createQuery(query).getResultList();
        }
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveClient(@Context UriInfo uriInfo, @Valid  ClientJpaImpl r) {
        log.info(r == null ? null:r.toString());
        URI newResourceUri;
        if (r == null || r.getNumber() != null) throw new BadRequestException();
        log.info(r.toString());
        try {
            dao.create(r);
            newResourceUri = uriInfo.getAbsolutePathBuilder().path(r.getNumber().toString()).build();
            //dao.delete(r);
            return Response.created(newResourceUri).entity(r).build();
        } catch (PersistenceException e) {

            UUID existingId = dao.findIdByEmail(r.getEmail());
            if (existingId != null) {
                newResourceUri = uriInfo.getAbsolutePathBuilder().path(existingId.toString()).build();
                return Response.seeOther(newResourceUri).build();
            } else return Response.status(Response.Status.CONFLICT).build();

        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteClient(@PathParam("id") UUID id) {
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
    public Response updateClient(ClientJpaImpl client, @PathParam("id") UUID id) {
        if (client == null || client.getNumber() != null && client.getNumber() != id) throw new BadRequestException();
        client.setNumber(id);
        try {
            dao.update(client);

        }
        catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException){
                return Response.status(Response.Status.CONFLICT).build();
            }
            else return Response.serverError().build();
        }
        return Response.noContent().build();

    }
/*
    @Path("{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ClientJpaImpl testpatch(@PathParam("id") UUID id, ClientJpaImpl client) {
        if (dao.findById(id) == null || client == null) {
            throw new NotFoundException();
        }
        dao.update(client);
        return client;
    }

 */
}