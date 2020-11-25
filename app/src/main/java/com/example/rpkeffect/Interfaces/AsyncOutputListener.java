package com.example.rpkeffect.Interfaces;

import com.example.rpkeffect.Constructors.Product;

import java.util.List;

public interface AsyncOutputListener {
    void onPostExecute(List<Product> products);
    void onUpdateListener(int value, int max);
}
