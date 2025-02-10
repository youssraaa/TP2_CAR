package com.store.controller;

import com.store.entity.Client;
import com.store.service.ClientService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/store")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/home")
    public String home() {
        return "home"; 
    }
    
    @PostMapping("/signup")
    public String signup(@RequestParam String email, @RequestParam String password,
                         @RequestParam String firstName, @RequestParam String lastName, 
                         RedirectAttributes redirectAttributes) {
        try {
            Client client = new Client(email, password, firstName, lastName);
            clientService.signup(client);
            redirectAttributes.addFlashAttribute("message", "Registration successful! You can now log in.");
            return "redirect:/store/home"; 
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/store/home"; 
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, 
                        HttpSession session, Model model) {
        try {
            Client client = clientService.login(email, password);
            session.setAttribute("clientEmail", client.getEmail()); 
            return "redirect:/store/welcome"; 
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "home"; 
        }
    }

    @GetMapping("/welcome")
    public String welcome(HttpSession session, Model model) {
        String clientEmail = (String) session.getAttribute("clientEmail"); 
        if (clientEmail == null) {
            return "redirect:/store/home"; 
        }
        Client currentUser = clientService.getClientByEmail(clientEmail);
        model.addAttribute("name", currentUser.getFirstName());
        return "welcome";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/store/home"; 
    }
}
