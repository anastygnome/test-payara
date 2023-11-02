package fr.univ.tln.tdomenge293.dao.jpa;

import fr.univ.tln.tdomenge293.model.jpa_impl.OrderJpaImpl;
import jakarta.persistence.EntityManagerFactory;

import java.util.UUID;

public class OrderDAO extends GenericDAO<OrderJpaImpl, UUID> {
    /**
     * Constructs a new {@code GenericDAO} instance.
     *
     * @param entityManagerFactory the entity manager factory for the given context
     */
    private OrderDAO(EntityManagerFactory entityManagerFactory) {
        super(OrderJpaImpl.class, entityManagerFactory);
    }

    public static OrderDAO of(EntityManagerFactory entityManagerFactory) {
        return new OrderDAO(entityManagerFactory);
    }
}
