package com.example.rpkeffect.Constructors;

public class Request {
    private String email;
    private String ip;
    private String date;

    public Request(String email, String ip, String date){
        this.email = email;
        this.ip = ip;
        this.date = date;
    }

    public Request(){}

    public String getEmail() {
        return email;
    }

    public String getIp() {
        return ip;
    }

    public String getDate() {
        return date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
