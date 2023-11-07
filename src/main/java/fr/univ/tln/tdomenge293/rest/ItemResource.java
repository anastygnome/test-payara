package fr.univ.tln.tdomenge293.rest;

import fr.univ.tln.tdomenge293.Main;
import fr.univ.tln.tdomenge293.dao.jpa.ItemDAO;
import fr.univ.tln.tdomenge293.model.jpa_impl.ItemJpaImpl;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import jakarta.validation.Valid;
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

@Path("item")
@Slf4j
public class ItemResource {
    private static final ItemDAO dao = ItemDAO.of(Main.emf);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ItemJpaImpl> getIt() {
        return dao.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ItemJpaImpl getitem(@PathParam("id") UUID id) {
        ItemJpaImpl result = dao.findById(id);
        if (result == null) throw new NotFoundException();
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveitem(@Context UriInfo uriInfo, ItemJpaImpl r) {
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
            UUID existingId = dao.findIDByName(r.getName());
            if (existingId != null) {
                newResourceUri = uriInfo.getAbsolutePathBuilder().path(existingId.toString()).build();
                return Response.seeOther(newResourceUri).build();
            } else return Response.status(Response.Status.CONFLICT).build();

        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteitem(@PathParam("id") UUID id) {
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
    public Response updateitem(ItemJpaImpl item, @PathParam("id") UUID id) {
        if (item == null || item.getNumber() != null && item.getNumber() != id) throw new BadRequestException();
        item.setNumber(id);
        try {
            dao.update(item);

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
