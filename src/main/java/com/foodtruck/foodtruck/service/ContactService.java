package com.foodtruck.foodtruck.service;

import java.util.List;

import com.foodtruck.foodtruck.entity.Contact;
import com.foodtruck.foodtruck.model.ContactModel;

public interface ContactService {
    public Contact addContact(ContactModel contactModel);

    public Contact findContact(String email);

    public List<Contact> getAllContacts();
}
