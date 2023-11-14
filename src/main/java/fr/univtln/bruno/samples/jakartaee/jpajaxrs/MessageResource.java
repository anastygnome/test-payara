package fr.univtln.bruno.samples.jakartaee.jpajaxrs;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Path("messages")
@ApplicationScoped
public class MessageResource {
	@Inject
	private MessageDAO messageDAO;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response persist(Message message) {
		log.info("REST request to persist message : {}", message);
		messageDAO.create(message);
		return Response.ok(message).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Message message) {
		log.info("REST request to update message : {}", message);
		messageDAO.edit(message);
		return Response.ok(message).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> findAll() {
		log.info("REST request to get all messages");
		return messageDAO.findAll();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response find(@PathParam("id") Long id) {
		log.info("REST request to get message : {}", id);
		Message person = messageDAO.find(id);
		if (person == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok(person).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response removePerson(@PathParam("id") Long id) {
		log.info("REST request to delete Person : {}", id);
		messageDAO.remove(messageDAO.find(id));
		return Response.ok().build();
	}
}