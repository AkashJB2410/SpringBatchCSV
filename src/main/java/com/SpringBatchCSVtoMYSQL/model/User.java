package com.SpringBatchCSVtoMYSQL.model;

public class User {
    private  int id ;
    private  String prename ;
    private  String firstname ;
    private  String lastname ;

    public User (){

    }

    public User(int id, String prename, String firstname, String lastname) {
        this.id = id;
        this.prename = prename;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
