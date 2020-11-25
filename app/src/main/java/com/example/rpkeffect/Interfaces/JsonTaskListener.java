package com.example.rpkeffect.Interfaces;

import com.example.rpkeffect.Constructors.Product;

import java.util.List;

public interface JsonTaskListener {
    void onSuccessListener(List<Product> products);
    void onUpdateProgressBarListener(int progress);
    void onSetMaxProgressBar(int max);
    void onAddProduct(Product product);
}
