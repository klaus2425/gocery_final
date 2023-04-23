package com.system.gocery_final.Model;

public class Users {
    private String firstName, lastName, contact, password, email, address, image;

    public Users() {

    }

    public Users(String firstName, String lastName, String contact, String password, String email, String address, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.password = password;
        this.email = email;
        this.address = address;
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
