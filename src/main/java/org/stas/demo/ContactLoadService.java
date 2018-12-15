package org.stas.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.stas.demo.persistence.model.Contact;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author stas ambartsumov
 * Contains csv-to-contacts parser methods
 */
@Service
public class ContactLoadService {
    private final static Logger logger = LoggerFactory.getLogger(ContactLoadService.class);

    /**
     * parse a collection of lines into collection of contacts
     * @param lines
     * @return
     */
    public Collection<Contact> parseContacts(Collection<String> lines) {
        Collection<Contact> contacts = new LinkedList<>();
        if (lines != null && !lines.isEmpty()) {
            for (String line : lines) {
                logger.debug("parsing line: {}", line);
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String name = parts[0];
                    String url = "";
                    switch (parts.length) {
                        case 3: {
                            name += parts[1];
                            url = parts[2];
                            break;
                        }
                        case 2:
                            url = parts[1];
                            break;
                    }
                    if (!url.isEmpty() && url.trim().startsWith("http")) {
                        logger.debug("parsed name: {}, and url: {}", name, url);
                        contacts.add(new Contact(name, url));
                    }
                }
            }

        }
        return contacts;
    }
}
