package org.stas.demo.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.stas.demo.persistence.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long>, ContactRepositoryCustom {


}
