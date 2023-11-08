package fr.univ.tln.tdomenge293.jpa.dto;

import fr.univ.tln.tdomenge293.Main;
import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
            Join<ClientJpaImpl, OrderJpaImpl> ordersJoin = clientRoot.join("orders", JoinType.INNER);

            criteriaQuery.select(ordersJoin.get("number")).where(cb.equal(clientRoot.get("number"), id));
            Set<UUID> results = em.createQuery(criteriaQuery).getResultStream().collect(Collectors.toSet());

            // Post-process to collect order UUIDs and construct the DTO
            if (results.isEmpty()) return null;
            // Constructing the DTO using the record constructor
            return new ClientJpaDto(c.getNumber(), c.getFirstName(), c.getLastName(), c.getEmail(), results);
        }
    }
}