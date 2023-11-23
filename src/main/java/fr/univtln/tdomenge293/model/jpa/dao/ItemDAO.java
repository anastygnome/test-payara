package fr.univtln.tdomenge293.model.jpa.dao;

import fr.univtln.tdomenge293.model.jpa.Commande;
import fr.univtln.tdomenge293.model.jpa.Produit;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;

@RequestScoped
public class ItemDAO extends GenericDAO<Produit, Long>{
    /**
     * Constructs a new {@code GenericDAO} instance.
     *
     */
    @PersistenceContext(unitName = "JpaPU")
    private EntityManager entityManager;

      ItemDAO() {
        super(Produit.class);
    }


    static ItemDAO of() {
        return new ItemDAO();
    }

    @PostConstruct
     private void init() {
        super.setEntityManager(entityManager);
    }
    public Produit findByName(String name) {
        return findByBusinessKey("name",name);
    }
    public Long findIDByName(String name) {
        return findIDByBusinessKey("name",name);
    }
}
