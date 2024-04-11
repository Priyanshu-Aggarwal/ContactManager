/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;
import java.util.Random;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model m) {
        m.addAttribute("title", "Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model m) {
        m.addAttribute("title", "About Us");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model m) {
        m.addAttribute("title", "Signup");
        m.addAttribute("user", new User());
        return "signup";
    }

    //custom Login
    @GetMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "Signin");
        return "login";
    }
//    this is for registering the user

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingresult, @RequestParam(value = "aggrement", defaultValue = "false") boolean aggrement,
            Model model, HttpSession session) {

        try {

            if (!aggrement) {
                System.out.println("Accept Terms and Conditions");
                throw new Exception("Accept Terms and Conditions");
            }

            if (bindingresult.hasErrors()) {
                model.addAttribute("user", user);
                System.out.println(bindingresult);
                return "signup";
            }
            user.setRole("ROLE_USER");
                 user.setIsEnabled(true);
            // System.out.println(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            session.setAttribute("User", user);
            

            User u = userRepository.getUserbyUserName(user.getEmail());
            if (u!=null)
            {
                 model.addAttribute("Error","Email already registered!!"); 
                 return "signup";
            } else {
                //send email otp
                Random r = new Random();
            int myOtp = r.nextInt(9999);
            System.out.println(myOtp);
                boolean flag = this.emailService.sendEmail(user.getEmail(), "OTP  " + myOtp, "Message From Contact Manager");
                if (flag) {
                    System.out.println("Email Sent");
                    session.setAttribute("message", new Message("OTP Sent Successfully! Please Check it", "alert-success"));
                    session.setAttribute("myOtp", myOtp);

                }
            }

            return "redirect:/verify-otp";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went Wrong!!" + e.getMessage(), "alert-danger"));
            return "signup";
        }

    }

    @GetMapping("/verify-otp")
    public String getOTPpage() {
        return "Verify_OTP";
    }

    @PostMapping("/process-verify")
    public String otpVerify(@RequestParam("OTP") int otp, Model model, HttpSession session) {
        //retriving user data from session
        User user = (User) session.getAttribute("User");

        //retriving otp from session
        int myotp = (int) session.getAttribute("myOtp");
        if (otp == myotp) {

            System.out.println("True and Valid");
            user.setIsEnabled(true);
            System.out.println(user);
            this.userRepository.save(user);
            session.setAttribute("message", new Message(" Well done.. Successfully Register:)", "alert-success"));
            return "redirect:/signup";
        } else {
            System.out.println("Error");
            return "redirect:/verify-otp";
        }

    }

}
