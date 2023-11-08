package fr.univ.tln.tdomenge293.jpa.dao;

import fr.univ.tln.tdomenge293.model.jpa_impl.ItemJpaImpl;
import jakarta.persistence.EntityManagerFactory;

import java.util.UUID;

public class ItemDAO extends GenericDAO<ItemJpaImpl, UUID>{
    /**
     * Constructs a new {@code GenericDAO} instance.
     *
     * @param entityManagerFactory the entity manager factory for the given context
     */
    private ItemDAO(EntityManagerFactory entityManagerFactory) {
        super(ItemJpaImpl.class, entityManagerFactory);
    }

    public static ItemDAO of(EntityManagerFactory entityManagerFactory) {
        return new ItemDAO(entityManagerFactory);
    }
    public ItemJpaImpl findByName(String name) {
        return findByBusinessKey("name",name);
    }
    public UUID findIDByName(String name) {
        return findIDByBusinessKey("name",name);
    }
}
