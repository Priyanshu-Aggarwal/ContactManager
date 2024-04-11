
package com.smart.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Entity
@Table(name="USER")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Name should not be blank")
    @Size(min=2,max=20,message = "Name should between 2 to 20 characters.")
    private String name;
    
    @Column(unique = true)
    @NotEmpty(message = "Please enter the email")
    private String email;
    
    @NotEmpty(message = "Please enter the password")
   
    private String password;
    private String role;
    private boolean isEnabled;
    private String imageURL;
    
    @Column(length = 500)
      @Size(max=20,message = "Max 200 characters are allowed.")
    private String about;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    private List<Contact> contacts=new ArrayList<>();

    public User() {
    }

    public List<Contact> getContact() {
        return contacts;
    }

    public void setContact(List<Contact> contact) {
        this.contacts = contact;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role + ", isEnabled=" + isEnabled + ", imageURL=" + imageURL + ", about=" + about + ", contacts=" + contacts + '}';
    }
    
}
