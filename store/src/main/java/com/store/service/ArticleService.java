package com.store.service;

import com.store.entity.Article;
import com.store.entity.Commande;
import com.store.repository.ArticleRepository;
import com.store.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommandeRepository commandeRepository;

    public void addArticleToCommande(Long commandeId, String libelle, int quantity, double unitPrice) {
        Commande commande = commandeRepository.findById(commandeId)
            .orElseThrow(() -> new RuntimeException("Commande not found"));

        Article article = new Article(libelle, quantity, unitPrice, commande);
        articleRepository.save(article);
    }

    public List<Article> getArticlesByCommandeId(Long commandeId) {
        return articleRepository.findByCommandeId(commandeId);
    }
    
    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
