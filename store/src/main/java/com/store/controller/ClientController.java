package com.store.controller;

import com.store.entity.Article;
import com.store.entity.Client;
import com.store.entity.Commande;
import com.store.service.ClientService;
import com.store.service.CommandeService;
import com.store.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/store")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ArticleService articleService;

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

        List<Commande> commandes = commandeService.getCommandesByClientEmail(clientEmail);
        model.addAttribute("commandes", commandes);

        return "welcome"; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/store/home"; 
    }

    @PostMapping("/addCommande")
    public String addCommande(@RequestParam String title, HttpSession session) {
        String clientEmail = (String) session.getAttribute("clientEmail");
        if (clientEmail != null) {
            commandeService.addCommande(clientEmail, title);
        }
        return "redirect:/store/welcome";
    }

    @GetMapping("/commande/{commandeId}/articles")
    public String viewCommandeArticles(@PathVariable Long commandeId, HttpSession session, Model model) {
        String clientEmail = (String) session.getAttribute("clientEmail");
        if (clientEmail == null) {
            return "redirect:/store/home";
        }

        Client currentUser = clientService.getClientByEmail(clientEmail);       
        Commande commande = commandeService.getCommandeById(commandeId);
        if (commande == null) {
            return "redirect:/store/welcome"; 
        }

        List<Article> articles = articleService.getArticlesByCommandeId(commandeId);
        
        model.addAttribute("name", currentUser.getFirstName()); 
        model.addAttribute("commande", commande); 
        model.addAttribute("articles", articles); 

        return "orderdetails"; 
    }

    @PostMapping("/commande/{commandeId}/addArticle")
    public String addArticle(@PathVariable Long commandeId, 
                             @RequestParam String libelle,
                             @RequestParam int quantity,
                             @RequestParam double unitPrice, 
                             HttpSession session) {
        String clientEmail = (String) session.getAttribute("clientEmail");
        if (clientEmail == null) {
            return "redirect:/store/home";
        }

        articleService.addArticleToCommande(commandeId, libelle, quantity, unitPrice);
        return "redirect:/store/commande/" + commandeId + "/articles";
    }
    
    @GetMapping("/commande/{commandeId}/articles/{articleId}/delete")
    public String deleteArticle(@PathVariable Long commandeId, @PathVariable Long articleId) {
        articleService.deleteArticle(articleId); 
        return "redirect:/store/commande/{commandeId}/articles"; 
    }
}
