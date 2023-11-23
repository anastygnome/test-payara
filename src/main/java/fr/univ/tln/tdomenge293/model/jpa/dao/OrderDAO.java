package fr.univ.tln.tdomenge293.model.jpa.dao;

import fr.univ.tln.tdomenge293.model.jpa.Commande;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RequestScoped  // or another appropriate scope
public class OrderDAO extends GenericDAO<Commande, Long> {

    @PersistenceContext(unitName = "JpaPU")
    private EntityManager entityManager;

    public OrderDAO() {
        super(Commande.class);
    }
    @PostConstruct
    public void init() {
        super.setEntityManager(entityManager);
    }
}