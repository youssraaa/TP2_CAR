package com.store.service;

import com.store.entity.Client;
import com.store.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

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
    
    public Client getClientByEmail(String email) {
        return clientRepository.findById(email)
            .orElseThrow(() -> new RuntimeException("Client not found"));
    }
}
