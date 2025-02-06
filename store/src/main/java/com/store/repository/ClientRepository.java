package com.store.repository;

import com.store.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

    public interface ClientRepository 
    extends CrudRepository<Client, String>{
}
