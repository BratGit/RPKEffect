package com.example.rpkeffect.Interfaces;

import com.example.rpkeffect.Constructors.Product;

import java.util.List;

public interface JsonTaskListener {
    void onUpdateProgressBarListener(int progress);
    void onSetMaxProgressBar(int max);
    void onAddProduct(Product product);
    void onFinish();
}
