/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@CrossOrigin("*")
@RequestMapping("api/explore")
public class DataController {
   
    static class Explore{
        public String FirstName;
        public String LastName;
        public String Email;
        public String Username;
        public String Password;
        public String Confirm;

        @Override
        public String toString() {
            return "Explore{" + "FirstName=" + FirstName + ", LastName=" + LastName + ", Email=" + Email + ", Username=" + Username + ", Password=" + Password + ", Confirm=" + Confirm + '}';
        }
        
    }
    
    @PostMapping
    public ResponseEntity<Explore> InsertExplore(@RequestBody Explore explore){
        System.out.println(explore.toString());
        return ResponseEntity.ok(explore);
    }
}
