package com.blade.ShopAR;

public class ShoppingCart {

    private int total;

    public ShoppingCart(){
        this.total = 0;
    }

    public int getTotal(){
        return this.total;
    }

    public void addToCart(int price){
        this.total += price;
    }

}
