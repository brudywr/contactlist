package org.stas.demo;

import org.junit.Before;
import org.junit.Test;
import org.stas.demo.persistence.model.Contact;

import java.util.Collection;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ContactLoadServiceTest {

    private ContactLoadService service = new ContactLoadService();
    private Collection<String> lines;

    @Before
    public void setUp() {
        lines = new LinkedList<>();
    }

    @Test
    public void shouldSkipEntriesWithoutValidUrl() {
        lines.add("name, url");
        lines.add("---------");
        lines.add("name, url, comment");

        Collection<Contact> results = service.parseContacts(lines);
        assertThat(results.toString(), equalTo("[]"));
    }

    @Test
    public void shouldAcceptUrl() {
        lines.add("name, http://url");
        lines.add("---------");
        lines.add("name, surname, http://comment");

        Collection<Contact> results = service.parseContacts(lines);
        assertThat(results.toString(), equalTo("[Contact[name=name, url= http://url], Contact[name=name surname, url= http://comment]]"));
    }

    @Test
    public void shouldTakeOnlyThreeTerms() {
        lines.add("name, surname, http://url");
        lines.add("name, surname, https://url, comment");

        Collection<Contact> results = service.parseContacts(lines);
        assertThat(results.toString(), equalTo("[Contact[name=name surname, url= http://url]]"));
    }
}
