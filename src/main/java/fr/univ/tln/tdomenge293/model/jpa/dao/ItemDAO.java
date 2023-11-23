package fr.univ.tln.tdomenge293.model.jpa.dao;

import fr.univ.tln.tdomenge293.model.jpa.Produit;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RequestScoped
public class ItemDAO extends GenericDAO<Produit, Long>{
    /**
     * Constructs a new {@code ItemDAO} instance.
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
