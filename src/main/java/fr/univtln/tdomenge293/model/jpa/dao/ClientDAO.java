package fr.univtln.tdomenge293.model.jpa.dao;

import fr.univtln.tdomenge293.model.jpa.Client;
import fr.univtln.tdomenge293.model.jpa.Commande;
import fr.univtln.tdomenge293.model.jpa.dto.ClientJpaDto;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequestScoped

public class ClientDAO extends GenericDAO<Client, Long> {
    @PersistenceContext(unitName = "JpaPU")
    private EntityManager entityManager;

    public ClientDAO() {
        super(Client.class);
    }
    @PostConstruct
    public void init() {
        super.setEntityManager(entityManager);
    }

    public Client findByEmail(String email) {
        return findByBusinessKey("email", email);
    }

    public Long findIdByEmail(String email) {
        return findIDByBusinessKey("email", email);
    }

    @Override
    public void delete(Client entity) {
        deleteByID(entity.getNumero());
    }
    public ClientJpaDto getDto(Long id) {
        Client c = entityManager.find(Client.class, id);
        if (c == null) return null;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Client> clientRoot = criteriaQuery.from(Client.class);
        Join<Client, Commande> ordersJoin = clientRoot.join("commandes");

        criteriaQuery.select(ordersJoin.get("numero")).where(cb.equal(clientRoot.get("numero"), id));
        Set<Long> results = entityManager.createQuery(criteriaQuery).getResultStream().collect(Collectors.toSet());

        // Post-process to collect order UUIDs and construct the DTO
        if (results.isEmpty()) return null;
        // Constructing the DTO using the record constructor
        return new ClientJpaDto(c.getNumero(), c.getPrenom(), c.getNom(), c.getEmail(), results);
    }
    public List<ClientJpaDto> getAllClientDtos() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

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
        for (Tuple tuple : entityManager.createQuery(criteriaQuery).getResultList()) {
            Client c = tuple.get("client", Client.class);
            Long orderNumber = tuple.get(ordersJoin.get("numero"));
            clientMap.computeIfAbsent(c.getNumero(), k -> ClientJpaDto.fromClient(c, new HashSet<>())).orders().add(orderNumber);
        }
        return List.copyOf(clientMap.values());
    }
}

