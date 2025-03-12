package com.store.service;

import com.store.entity.Commande;
import com.store.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public void addCommande(String clientEmail, String title) {
        Commande commande = new Commande(title, clientEmail);
        commandeRepository.save(commande);
    }

    public List<Commande> getCommandesByClientEmail(String clientEmail) {
        return commandeRepository.findByClientEmail(clientEmail);
    }
    
    public Commande getCommandeById(Long commandeId) {
        return commandeRepository.findById(commandeId).orElse(null);
    }

}
