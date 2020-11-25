package com.example.rpkeffect.Constructors;

public class Product {
    String name;
    String status;
    String article;
    String code;

    public Product(String name, String status, String article, String code) {
        this.name = name;
        this.status = status;
        this.article = article;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
