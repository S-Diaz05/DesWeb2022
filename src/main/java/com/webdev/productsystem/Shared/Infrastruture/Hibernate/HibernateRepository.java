package com.webdev.productsystem.Shared.Infrastruture.Hibernate;

import com.webdev.productsystem.Shared.Domain.Aggregate.CustomUUID;
import com.webdev.productsystem.Tours.Hotel.Domain.Hotel;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class HibernateRepository<T> {

    protected final SessionFactory sessionFactory;
    protected final Class<T> aggregateClass;

    public HibernateRepository(SessionFactory sessionFactory, Class<T> aggregateClass) {
        this.sessionFactory = sessionFactory;
        this.aggregateClass = aggregateClass;
    }

    protected void persist(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

    protected Optional<List<T>> getAll() {
        CriteriaQuery<T> criteria = sessionFactory.getCriteriaBuilder().createQuery(aggregateClass);
        criteria.from(aggregateClass);
        return Optional.ofNullable(sessionFactory.getCurrentSession().createQuery(criteria).getResultList());
    }

    protected Optional<T> byId(CustomUUID id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().byId(aggregateClass).load(id));
    }

    protected Optional<T> byName(String name) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(aggregateClass);
        Root<T> root = criteriaQuery.from(aggregateClass);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
        List<T> result = sessionFactory
                .getCurrentSession()
                .createQuery(criteriaQuery)
                .getResultList();
        return Optional.ofNullable(result.get(0));
    }

    protected void updateEntity(T entity) {
        sessionFactory.getCurrentSession().update(entity);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

    protected void deleteEntity(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }
}
