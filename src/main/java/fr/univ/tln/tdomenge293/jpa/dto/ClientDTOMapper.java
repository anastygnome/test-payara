package fr.univ.tln.tdomenge293.jpa.dto;

import fr.univ.tln.tdomenge293.Main;
import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;

import java.util.*;
import java.util.stream.Collectors;

import static fr.univ.tln.tdomenge293.jpa.dto.ClientJpaDto.fromClient;

public class ClientDTOMapper {

    private ClientDTOMapper() {
    }

    public static ClientJpaDto getDto(UUID id) {
        try (EntityManager em = Main.emf.createEntityManager()) {
            ClientJpaImpl c = em.find(ClientJpaImpl.class, id);
            if (c == null) return null;
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<UUID> criteriaQuery = cb.createQuery(UUID.class);
            Root<ClientJpaImpl> clientRoot = criteriaQuery.from(ClientJpaImpl.class);
            Join<ClientJpaImpl, OrderJpaImpl> ordersJoin = clientRoot.join("orders");

            criteriaQuery.select(ordersJoin.get("number")).where(cb.equal(clientRoot.get("number"), id));
            Set<UUID> results = em.createQuery(criteriaQuery).getResultStream().collect(Collectors.toSet());

            // Post-process to collect order UUIDs and construct the DTO
            if (results.isEmpty()) return null;
            // Constructing the DTO using the record constructor
            return new ClientJpaDto(c.getNumber(), c.getFirstName(), c.getLastName(), c.getEmail(), results);
        }
    }

    public static List<ClientJpaDto> getAllClientDtos() {
        try (EntityManager em = Main.emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // Creating a CriteriaQuery for a Tuple
            CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
            Root<ClientJpaImpl> clientRoot = criteriaQuery.from(ClientJpaImpl.class);
            Join<ClientJpaImpl, OrderJpaImpl> ordersJoin = clientRoot.join("orders",JoinType.LEFT);

            // Selecting client fields and order number
            criteriaQuery.multiselect(
                    clientRoot.alias("client"),
                    ordersJoin.get("number")
            );

            // Grouping by Client and Collecting Order UUIDs
            Map<UUID, ClientJpaDto> clientMap = new HashMap<>();
            for (Tuple tuple : em.createQuery(criteriaQuery).getResultList()) {
                ClientJpaImpl c  = tuple.get("client", ClientJpaImpl.class);
                UUID orderNumber = tuple.get(ordersJoin.get("number"));
                clientMap.computeIfAbsent(c.getNumber() , k -> ClientJpaDto.fromClient(c,new HashSet<>())).orders().add(orderNumber);
            }
            return  List.copyOf(clientMap.values());
        }
    }
}