package com.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.store.entity.Article;
import com.store.entity.Client;
import com.store.entity.Commande;
import com.store.service.ArticleService;
import com.store.service.ClientService;
import com.store.service.CommandeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/store")
public class CommandeController {
	
	@Autowired
    private ClientService clientService;

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ArticleService articleService;

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
        if (commande == null || !commande.getClientEmail().equals(currentUser.getEmail())){
            return "redirect:/store/welcome"; 
        }

        List<Article> articles = articleService.getArticlesByCommandeId(commandeId);
        
        model.addAttribute("name", currentUser.getFirstName()); 
        model.addAttribute("commande", commande); 
        model.addAttribute("articles", articles); 

        return "orderdetails"; 
    }
    
    @GetMapping("/commande/{commandeId}/imprimer")
    public String printCommande(@PathVariable Long commandeId, Model model, HttpSession session) {
    	
    	 String clientEmail = (String) session.getAttribute("clientEmail");

        Commande commande = commandeService.getCommandeById(commandeId);
        if (commande == null || clientEmail == null || !commande.getClientEmail().equals(clientEmail)) {
            return "redirect:/store/welcome"; 
        }

        List<Article> articles = articleService.getArticlesByCommandeId(commandeId);

        Client client = clientService.getClientByEmail(commande.getClientEmail());

        double totalCommande = articles.stream()
            .mapToDouble(article -> article.getQuantity() * article.getUnitPrice())
            .sum();

        model.addAttribute("commande", commande);
        model.addAttribute("articles", articles);
        model.addAttribute("client", client);
        model.addAttribute("totalCommande", totalCommande);

        return "print"; 
    }
}
