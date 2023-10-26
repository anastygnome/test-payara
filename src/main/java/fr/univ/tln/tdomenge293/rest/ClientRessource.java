package fr.univ.tln.tdomenge293.rest;

import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static fr.univ.tln.tdomenge293.Main.emf;

@Slf4j
@Path("client")

public class ClientRessource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<ClientJpaImpl> getClients() {
        Set<ClientJpaImpl> result = new HashSet<>();
        try (
                EntityManager em = emf.createEntityManager()) {
            result = em.createQuery("FROM ClientJpaImpl", ClientJpaImpl.class).getResultStream().collect(Collectors.toSet());
        }
        return result;

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ClientJpaImpl getclient(@PathParam("id") UUID id) {
        ClientJpaImpl result = null;
        try (
                EntityManager em = emf.createEntityManager()) {
            result = em.find(ClientJpaImpl.class, id);
        }
        if (result == null) throw new NotFoundException();
        return result;
    }

    @POST
    @Consumes
    public void receiveClient(ClientJpaImpl r) {
        try (
                EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
        }
        log.info(r.toString());
    }

}
