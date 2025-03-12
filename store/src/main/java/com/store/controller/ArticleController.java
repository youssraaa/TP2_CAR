package com.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.store.service.ArticleService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/store")
public class ArticleController {
    
	@Autowired
	private ArticleService articleService;
	
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
