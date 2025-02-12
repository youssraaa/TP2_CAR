package com.store.service;

import com.store.entity.Client;
import com.store.entity.Commande;
import com.store.repository.ClientRepository;
import com.store.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CommandeRepository commandeRepository;

    public void signup(Client client) {
        if (clientRepository.findById(client.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        clientRepository.save(client);
    }

    public Client login(String email, String password) {
        Optional<Client> optionalClient = clientRepository.findById(email);
        if (optionalClient.isEmpty()) {
            throw new RuntimeException("Invalid email");
        }

        Client client = optionalClient.get();
        if (!client.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return client;
    }

    public void addCommande(String clientEmail, String title) {
        Commande commande = new Commande(title, clientEmail);
        commandeRepository.save(commande);
    }

    public List<Commande> getCommandesByClientEmail(String clientEmail) {
        return commandeRepository.findByClientEmail(clientEmail);
    }

    public Client getClientByEmail(String email) {
        return clientRepository.findById(email)
            .orElseThrow(() -> new RuntimeException("Client not found"));
    }
}
