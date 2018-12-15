package org.stas.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.stas.demo.persistence.model.Contact;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author stas ambartsumov
 * contact list controller
 */
@Controller
public class ContactListController {

    private final static Logger logger = LoggerFactory.getLogger(ContactListController.class);

    @Autowired
    private ContactListService contactListService;
    @Autowired
    private MessageService messageService;

    @Value("${page.size:5}")
    private int pageSize;

    @RequestMapping({"/", "/index"})
    public String index(Model model, @RequestParam(name = "search", required = false, defaultValue = "") String search,
                        @RequestParam("page") Optional<Integer> page) {
        logger.info("contact list index");

        //todo - refactor so that it would not actually fetch data from DB
        int currentPage = page.orElse(1);
        Page<Contact> contactPage = contactListService.findPaginated(search, PageRequest.of(currentPage - 1, pageSize));

        logger.info("found {} elements", contactPage.getTotalElements());
        model.addAttribute("contactPage", contactPage);

        int totalPages = contactPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("search", search);
        }

        return "index";
    }

    @GetMapping("/contacts")
    @ResponseBody
    public Page<Contact> listContacts(Model model,
                                      @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                      @RequestParam(name ="page", required = false) Integer page) {
        int currentPage = (page != null ? page : 1);
        Page<Contact> contactPage = contactListService.findPaginated(search, PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("search", search);

        return contactPage;
    }

    @PostMapping("/contact/add")
    @ResponseBody
    public Response addContact(@ModelAttribute("contactForm") ContactForm contactForm, BindingResult bindingResult) {
        String userName = contactForm.getName();
        String pictureUrl = contactForm.getUrl();

        if (userName != null && userName.length() > 0) {
            return new Response("OK", contactListService.addContact(userName, pictureUrl));
        }

        return new Response(messageService.getMessage("add.contact.error.message"), null);
    }

    @GetMapping("/cleanContacts")
    public String cleanContacts() {
        contactListService.cleanContacts();
        return "redirect:/";
    }
}