package com.example.rpkeffect.Constructors;

public class User {
    private String name;
    private String image;
    private String mail;
    private String date;
    private String ip;

    public User(String name, String image, String mail, String date, String ip){
        this.name = name;
        this.image = image;
        this.mail = mail;
        this.date = date;
        this.ip = ip;
    }

    public User(){}

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getIp() {
        return ip;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }
}
