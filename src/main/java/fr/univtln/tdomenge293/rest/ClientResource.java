package fr.univtln.tdomenge293.rest;

import fr.univtln.tdomenge293.model.jpa.Client;
import fr.univtln.tdomenge293.model.jpa.Commande;
import fr.univtln.tdomenge293.model.jpa.dao.ClientDAO;
import fr.univtln.tdomenge293.model.jpa.dto.ClientDTOMapper;
import fr.univtln.tdomenge293.model.jpa.dto.ClientJpaDto;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
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

@Slf4j
@Path("clients")
@RequestScoped
public class ClientResource {
    @Inject
    ClientDAO clientDAO;
    @PersistenceContext(unitName = "JpaPU")
    EntityManager em;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Client> getIt() {
        return clientDAO.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ClientJpaDto getclient(@PathParam("id") Long id) {
       ClientJpaDto resultat = clientDAO.getDto(id);
        if (resultat == null) throw new NotFoundException();
        return resultat;
    }
}
/*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveClient(@Context UriInfo uriInfo, @Valid ClientJpaImpl r) {
        log.info(r == null ? null : r.toString());
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

        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                return Response.status(Response.Status.CONFLICT).build();
            } else return Response.serverError().build();
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

}
*/
