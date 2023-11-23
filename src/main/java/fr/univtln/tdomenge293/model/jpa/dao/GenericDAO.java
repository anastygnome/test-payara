package fr.univtln.tdomenge293.model.jpa.dao;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;

abstract class GenericDAO<T, K extends Serializable> {
    /**
     * A generic DAO class to manage entities of type {@code T} with primary keys of type {@code K}.
     */
    private  EntityManager entityManager;
    private final Class<T> entityClass;
    private  Class<K> keyClass;
    private  String idAttributeName;
    private boolean setEm = false;
    /**
     * Constructs a new {@code GenericDAO} instance.
     *
     * @param entityClass the class of the entity
     */
    GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    void setEntityManager(EntityManager entityManager) {
        if (setEm) {
            throw new IllegalStateException("Trying to set t");
        }
        this.entityManager = entityManager;
        EntityType<T> entityType = entityManager.getMetamodel().entity(entityClass);
        try {
            @SuppressWarnings("unchecked")
            // SAFETY: The following cast is unchecked because Java's type erasure means
            // we don't have direct runtime information about the generic type K.
            // we trust JPA gives us information here
            Class<K> keyClassCast = (Class<K>) entityType.getIdType().getJavaType();
            this.keyClass = keyClassCast;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Expected ID type " + entityType.getIdType().getJavaType() + " but got", e);
        }
        this.idAttributeName = entityType.getId(keyClass).getName();
        setEm = true;

    }
@Transactional(Transactional.TxType.REQUIRED)
    public T findById(K id) {
        return entityManager.find(entityClass, id);
    }
    @Transactional(Transactional.TxType.REQUIRED)

    public List<T> findAll(int pageNumber, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        if (pageNumber > 0) allQuery.setFirstResult((pageNumber - 1) * pageSize);
        if (pageSize >= 0) allQuery.setMaxResults(pageSize);
        return allQuery.getResultList();

    }
    @Transactional(Transactional.TxType.REQUIRED)

    public List<T> findAll() {
        return findAll(-1, -1);
    }
    @Transactional(Transactional.TxType.REQUIRED)

    public Long getCount() {
        CriteriaQuery<Long> cq = entityManager.getCriteriaBuilder().createQuery(Long.class);
        cq.select(entityManager.getCriteriaBuilder().count(cq.from(entityClass)));
        return entityManager.createQuery(cq).getSingleResult();
    }
    @Transactional(Transactional.TxType.REQUIRED)

    public T create(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        entityManager.flush();
        return entity;
    }
    @Transactional(Transactional.TxType.REQUIRED)

    public T update(T entity) {
        entityManager.getTransaction().begin();
        T mergedEntity = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return mergedEntity;
    }
    @Transactional(Transactional.TxType.REQUIRED)

    public void delete(T entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        entityManager.getTransaction().commit();
    }
    @Transactional(Transactional.TxType.REQUIRED)

    public void deleteByID(K id) {
        if (id == null) throw new IllegalArgumentException("Attempt to delete with a null key");

        entityManager.getTransaction().begin();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(entityClass);

        Root<T> root = criteriaDelete.from(entityClass);
        criteriaDelete.where(criteriaBuilder.equal(root.get(idAttributeName), id));
        int count = entityManager.createQuery(criteriaDelete).executeUpdate();

        if (count != 1) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Delete by ID failed");
        }
        entityManager.getTransaction().commit();
    }
    @Transactional(Transactional.TxType.REQUIRED)

    K findIDByBusinessKey(String attributeName, Object value) {
        try {
            entityManager.getTransaction().begin();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<K> cq = cb.createQuery(keyClass);
            Root<T> rootEntry = cq.from(entityClass);
            cq.select(rootEntry.get(idAttributeName));
            cq.where(cb.equal(rootEntry.get(attributeName), value));
            TypedQuery<K> userQuery = entityManager.createQuery(cq);
            return userQuery.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new NoResultException("Couldn't find the primary key with parameters (" + attributeName + " = " + value + ").");
        }

    }
    @Transactional(Transactional.TxType.REQUIRED)

    T findByBusinessKey(String attributeName, Object value) {

        try {
            entityManager.getTransaction().begin();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> rootEntry = cq.from(entityClass);
            cq.select(rootEntry.get(idAttributeName));
            cq.where(cb.equal(rootEntry.get(attributeName), value));
            TypedQuery<T> userQuery = entityManager.createQuery(cq);
            return userQuery.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new NoResultException("Couldn't find the entity with parameters (" + attributeName + " = " + value + ").");
        }
    }
}
