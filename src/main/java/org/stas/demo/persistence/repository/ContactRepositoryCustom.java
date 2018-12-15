package org.stas.demo.persistence.repository;

import org.stas.demo.persistence.model.Contact;

/**
 * @author stas ambartsumov
 * defines custom contact repository methods
 */
public interface ContactRepositoryCustom {
    /**
     * find all entities with a name field value similar to search argument
     * @param search
     * @return
     */
    Iterable<Contact> findAllSimilar(String search);
}
