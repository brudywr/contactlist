package org.stas.demo;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    //todo - to make it really work (for now it's blocked from the client-side anyway)
    @ExceptionHandler(MultipartException.class)
    public String handleError(MultipartException e, RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("message", e.getCause().getMessage());
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/uploadFile";

    }
}
