package fr.univtln.bruno.samples.jakartaee.jpajaxrs;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import javax.sql.DataSource;

import java.util.List;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@RequestScoped
public class MessageDAO {

    @PersistenceContext
    private EntityManager em;



    @Transactional(REQUIRED)
    public void create(Message message) {
        em.persist(message);
    }

    @Transactional(REQUIRED)
    public Message edit(Message message) {
        return em.merge(message);
    }

    @Transactional(REQUIRED)
    public void remove(Message message) {
        em.remove(em.merge(message));
    }

    public Message find(Long id) {
        return em.find(Message.class, id);
    }

    public List<Message> findAll() {
        return em.createNamedQuery("Message.findAll", Message.class).getResultList();
    }
}
