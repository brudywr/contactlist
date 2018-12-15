package org.stas.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.stas.demo.persistence.model.Contact;
import org.stas.demo.persistence.repository.ContactRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author stas ambartsumov
 * contains basic contact list operations
 */
@Service
public class ContactListService {

    private final static Logger logger = LoggerFactory.getLogger(ContactListService.class);

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ContactLoadService contactLoadService;

    /**
     * fetches contacts from DB and extracts a specific page. If search is defined it fetches only the contacts with
     * similar name
     * @param search
     * @param pageable
     * @return
     */
    @Transactional
    public Page<Contact> findPaginated(String search, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Contact> contacts = StreamSupport.stream(contactRepository.findAllSimilar(search).spliterator(), false)
                .collect(Collectors.toList());

        List<Contact> list;
        if (contacts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, contacts.size());
            list = contacts.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), contacts.size());
    }

    /**
     * uploads the contacts csv file to a temporary storage, parses it into contacts and then push it to the DB
     * @param file
     * @return
     */
    Collection<Contact> loadContactsFromFile(MultipartFile file) {
        logger.info("loading contacts from file: " + file.getName());
        Path path = null;
        Collection<Contact> contacts = new LinkedList<>();
        try {
            path = Files.createTempFile("uploaded-contacts", ".csv");
            Files.write(path, file.getBytes());
            Collection<String> lines = Files.lines(path).collect(Collectors.toList());
            path.toFile().delete();

            contacts = contactLoadService.parseContacts(lines);
        } catch (IOException e) {
            logger.error("an IO error occurred while loading contacts file: " + file.getOriginalFilename());
            e.printStackTrace();
        } finally {
            if (path != null) {
                path.toFile().deleteOnExit();
            }
        }

        return contacts;
    }

    /**
     * pushes a collection of contacts to DB
     * @param contacts
     */
    @Transactional
    public void saveContacts(Collection<Contact> contacts) {
        contactRepository.saveAll(contacts);
    }

    /**
     * drops all contacts from DB
     */
    @Transactional
    public void cleanContacts() {
        contactRepository.deleteAll();
    }

    /**
     * creates a contact with specified name and url and pushes it to DB
     * @param userName
     * @param pictureUrl
     */
    @Transactional
    public Contact addContact(String userName, String pictureUrl) {
        Contact newContact = new Contact(userName, pictureUrl);
        return contactRepository.save(newContact);
    }
}
