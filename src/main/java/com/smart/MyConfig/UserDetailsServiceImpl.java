/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.MyConfig;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements  UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        //fetching user from database
        User user=userRepository.getUserbyUserName(username);
        if(user==null)
        {
            throw new UsernameNotFoundException("Could Not find User with this Email");
        }
        CustomUserDetails customUserDetails=new CustomUserDetails(user);
        return customUserDetails;
    }
    
}
