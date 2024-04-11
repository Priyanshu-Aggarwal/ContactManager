/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.controller;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class searchController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
//    search handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal)
    {
        User user=this.userRepository.getUserbyUserName(principal.getName());
       List<Contact> contacts= this.contactRepository.findByNameContainingAndUser(query, user);
        return ResponseEntity.ok(contacts);
    }
    
    
    
}
