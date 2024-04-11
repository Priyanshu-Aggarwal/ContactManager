package com.smart.controller;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;
    //common data which will share acroos all the handlers.

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        System.out.println(principal.getName());
        User user = userRepository.getUserbyUserName(principal.getName());
        model.addAttribute("User", principal.getName());

    }

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {

        return "normal/user_dashboard";
    }

    //add Contact form 
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add contact");
        model.addAttribute("contact", new Contact());

        return "normal/addContactForm";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, Principal principal, HttpSession session, @RequestParam("profileImage") MultipartFile file) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserbyUserName(name);
         

//       processing and uploading file
            if (file.isEmpty()) {

                System.out.println("Emply File");
                contact.setImage("contact.png");
            } else 
            {
                //upload file to folder
                
                String filename=contact.getName()+"."+file.getOriginalFilename();
                contact.setImage(filename);
                File saveFile=new ClassPathResource("static/img").getFile();
              Path path=  Paths.get(saveFile.getAbsolutePath()+File.separator+filename);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                
            }

            contact.setUser(user);
            user.getContact().add(contact);
            this.userRepository.save(user);
            System.out.println("Added to database");
            session.setAttribute("message", new Message("Successfully Added", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong.. Please verify data again", "alert-danger"));
        }
        return "normal/addContactForm";
    }
    
    //show contact handler
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page,Model m, Principal principal)
    {
        m.addAttribute("title","show All Contacts");
        
        //contacts ki list bhejni hai
        String Username=principal.getName();
        User u=this.userRepository.getUserbyUserName(Username);
       int id= u.getId();
       //current page=page
       //contact per page=5
       Pageable pageable= PageRequest.of(page,5);
       
        Page<Contact> contacts=this.contactRepository.findContactByUser(id,pageable);
        m.addAttribute("Contacts", contacts);
        m.addAttribute("CurrentPage",page);
        m.addAttribute("TotalPages",contacts.getTotalPages());
        return "normal/showContact";
    }
    
//    particular contact details
    
    @GetMapping("/contact/{cId}")
    public String showContactDetails(@PathVariable("cId") Integer cId,Model m)
    {
        Optional<Contact> contactOptional=this.contactRepository.findById(cId);
       Contact contact= contactOptional.get();
       m.addAttribute("contact", contact);
        return "normal/contact_details";
    }
   
    @GetMapping("/update-Contact/{cId}")
    public String showUpdateContactPage(@PathVariable Integer cId,Model m)        
    {   
        
        Contact contact=contactRepository.findById(cId).orElse(null);
        m.addAttribute("contact",contact);
       
        return "normal/updateContact";
    }
    
    @PostMapping("/process-update")
    public String processUpdate(@ModelAttribute Contact contact,HttpSession session,
            @RequestParam("profileImage") MultipartFile file,Principal principal)
    {
          Contact oldContactDetail= contactRepository.findById(contact.getcId()).get();
        try{
        if(!file.isEmpty())
        {
//            save or rewrite the new image or file
            
//            delete old photo
            File deleteFile=new ClassPathResource("static/img").getFile();
            File f=new File(deleteFile,oldContactDetail.getImage());
            f.delete();
            
            
            
//            update new photo
                String filename=contact.getName()+"."+file.getOriginalFilename();
                contact.setImage(filename);
                File saveFile=new ClassPathResource("static/img").getFile();
                Path path=  Paths.get(saveFile.getAbsolutePath()+File.separator+filename);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);   
        }
        else
        {
         contact.setImage(oldContactDetail.getImage());
        }      
               
                
                User user=this.userRepository.getUserbyUserName(principal.getName());
              
                contact.setUser(user);
                this.contactRepository.save(contact);
                session.setAttribute("message", new Message("Successfully Updated", "alert-success"));
            
        
        } catch (Exception ex) {
              ex.printStackTrace();
            }
         return "redirect:/user/show-contacts/0";
    }
    
    
    @GetMapping("/delete/{cid}")
    public String delete(@PathVariable("cid") Integer cid,HttpSession session)
    {
        contactRepository.deleteById(cid);
       session.setAttribute("message", new Message("Delete Successfully! Please Check it", "alert-success"));
       session.setMaxInactiveInterval(10);//inactivity of user for 10 min. then it will logout
        return  "redirect:/user/show-contacts/0";
    }
}
