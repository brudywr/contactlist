package org.stas.demo.persistence.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.stas.demo.persistence.model.Contact;

import java.util.Collection;
import java.util.LinkedList;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContactRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void shouldPersistEntry() {
        entityManager.persistAndFlush(new Contact("stas", "test"));

        Contact result = contactRepository.findAll().iterator().next();
        assertThat(result, notNullValue());
        assertThat(result.toString(), equalTo("Contact[name=stas, url=test]"));
    }

    @Test
    public void shouldSearchSimilarEntries() {
        entityManager.persistAndFlush(new Contact("stas", "test"));
        entityManager.persistAndFlush(new Contact("Stas", "test"));
        entityManager.persistAndFlush(new Contact("stanislav", "test"));
        entityManager.persistAndFlush(new Contact("something else", "test"));
        entityManager.persistAndFlush(new Contact("more stas", "test"));

        Collection<String> names = new LinkedList<>();
        contactRepository.findAllSimilar("sta").forEach(contact -> names.add(contact.getName()));
        assertThat(names.toString(), equalTo("[stas, Stas, stanislav, more stas]"));
    }

}
