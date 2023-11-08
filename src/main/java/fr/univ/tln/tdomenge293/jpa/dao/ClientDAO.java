package fr.univ.tln.tdomenge293.jpa.dao;

import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import jakarta.persistence.EntityManagerFactory;

import java.util.UUID;

public class ClientDAO extends GenericDAO<ClientJpaImpl, UUID> {

    private ClientDAO(EntityManagerFactory entityManagerFactory) {
        super(ClientJpaImpl.class, entityManagerFactory);
    }

    public static ClientDAO of(EntityManagerFactory entityManagerFactory) {
        return new ClientDAO(entityManagerFactory);
    }

    public ClientJpaImpl findByEmail(String email) {
        return findByBusinessKey("email", email);
    }

    public UUID findIdByEmail(String email) {
        return findIDByBusinessKey("email", email);
    }

    @Override
    public void delete(ClientJpaImpl entity) {
        deleteByID(entity.getNumber());
    }


}

