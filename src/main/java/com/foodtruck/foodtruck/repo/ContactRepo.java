package com.foodtruck.foodtruck.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodtruck.foodtruck.entity.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long> {
    public Contact findByName(String email);
}
