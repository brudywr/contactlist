package org.stas.demo.persistence.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.stas.demo.persistence.model.Contact;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

/**
 * @author stas ambartsumov
 * implements custom contact repository methods
 */
@Repository
@Transactional(readOnly = true)
public class ContactRepositoryImpl implements ContactRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Iterable<Contact> findAllSimilar(String search) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> query = builder.createQuery(Contact.class);
        Root<Contact> root = query.from(Contact.class);

        Expression<String> name = root.get("name");
        Expression<String> lowerName = builder.lower(name);
        Predicate predicate = builder.like(lowerName, String.format("%%%s%%", search.toLowerCase()));
        query.where(builder.and(predicate));

        return entityManager.createQuery(query.select(root)).getResultList();
    }
}
