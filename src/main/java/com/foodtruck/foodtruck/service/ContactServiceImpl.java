package com.foodtruck.foodtruck.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.foodtruck.foodtruck.entity.Contact;
import com.foodtruck.foodtruck.entity.FoodtruckEntity;
import com.foodtruck.foodtruck.entity.UserEntity;
import com.foodtruck.foodtruck.model.ContactModel;
import com.foodtruck.foodtruck.repo.ContactRepo;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    ContactRepo contactRepo;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    FoodTruckServiceImpl foodTruckServiceImpl;

    @Override
    public Contact addContact(ContactModel contactModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        Contact contact = new Contact();
        if (role.contains("ROLE_USER")) {
            UserEntity user = userServiceImpl.findUser(authentication.getName());
            contact.setName(user.getName());

        } else {
            FoodtruckEntity foodtruckEntity = foodTruckServiceImpl.findFoodTruckByEmail(authentication.getName());
            contact.setName(foodtruckEntity.getName());
        }

        contact.setMsg(contactModel.getMsg());
        return contactRepo.save(contact);
    }

    @Override
    public Contact findContact(String email) {
        return contactRepo.findByName(email);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

}
