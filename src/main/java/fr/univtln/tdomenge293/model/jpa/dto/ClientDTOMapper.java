package fr.univtln.tdomenge293.model.jpa.dto;

import fr.univtln.tdomenge293.model.jpa.Client;
import fr.univtln.tdomenge293.model.jpa.Commande;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
public class ClientDTOMapper {
    @PersistenceContext(unitName = "JpaPU")
     EntityManager em;

    ClientDTOMapper() {
    }

    public  ClientJpaDto getDto(Long id) {
        Client c = em.find(Client.class, id);
        if (c == null) return null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Client> clientRoot = criteriaQuery.from(Client.class);
        Join<Client, Commande> ordersJoin = clientRoot.join("commandes");

        criteriaQuery.select(ordersJoin.get("numero")).where(cb.equal(clientRoot.get("numero"), id));
        Set<Long> results = em.createQuery(criteriaQuery).getResultStream().collect(Collectors.toSet());

        // Post-process to collect order UUIDs and construct the DTO
        if (results.isEmpty()) return null;
        // Constructing the DTO using the record constructor
        return new ClientJpaDto(c.getNumero(), c.getPrenom(), c.getNom(), c.getEmail(), results);
    }

    public  List<ClientJpaDto> getAllClientDtos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Creating a CriteriaQuery for a Tuple
        CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
        Root<Client> clientRoot = criteriaQuery.from(Client.class);
        Join<Client, Commande> ordersJoin = clientRoot.join("commandes", JoinType.LEFT);

        // Selecting client fields and order number
        criteriaQuery.multiselect(
                clientRoot.alias("client"),
                ordersJoin.get("numero")
        );

        // Grouping by Client and Collecting Order UUIDs
        Map<Long, ClientJpaDto> clientMap = new HashMap<>();
        for (Tuple tuple : em.createQuery(criteriaQuery).getResultList()) {
            Client c = tuple.get("client", Client.class);
            Long orderNumber = tuple.get(ordersJoin.get("numero"));
            clientMap.computeIfAbsent(c.getNumero(), k -> ClientJpaDto.fromClient(c, new HashSet<>())).orders().add(orderNumber);
        }
        return List.copyOf(clientMap.values());
    }
}