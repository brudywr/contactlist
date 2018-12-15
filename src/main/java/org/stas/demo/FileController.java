package org.stas.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.stas.demo.persistence.model.Contact;

import java.util.Collection;

/**
 * @author stas ambartsumov
 * controller for file related operations
 */
@Controller
public class FileController {
    private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private ContactListService contactListService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/uploadFile")
    public String showOpenFilePage(Model model) {
        logger.info("showing file upload page");
        return "uploadFile";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            logger.error(messageService.getMessage("file.select.message"));
            redirectAttributes.addFlashAttribute("message", messageService.getMessage("file.select.message"));

            return "redirect:uploadFile";
        }
        try {
            Collection<Contact> contacts = contactListService.loadContactsFromFile(file);
            logger.info("loaded {} contacts from {}", contacts.size(), file.getOriginalFilename());
            if (!contacts.isEmpty()) {
                contactListService.saveContacts(contacts);
                logger.info("contacts pushed to DB");
                redirectAttributes.addFlashAttribute("message", messageService.getMessage("file.upload.success")
                        + file.getOriginalFilename());
                redirectAttributes.addAttribute("fileName", file.getOriginalFilename());
            }
        } catch (Exception e) {
            logger.error("an error occurred while loading csv file", e);
            redirectAttributes.addFlashAttribute("message", messageService.getMessage("file.upload.fail"));
        }

        return "redirect:/";
    }
}