package com.blade.ShopAR;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private double total;
    private Map<WegmansData, Integer> cart;

    public ShoppingCart(){
        this.cart = new HashMap<>();
        this.total = 0;
    }

    public double getTotal() {
        return this.total;
    }

    public double recountTotal(){

        int total = 0;

        for(WegmansData d: this.cart.keySet())
            total += Double.valueOf(d.getPrice()) * this.cart.get(d);

        return this.total;
    }

    public void addToCart(WegmansData d){
        if (d == null)
                return;
        
        this.total += Double.valueOf(d.getPrice());

        if(this.cart.containsKey(d)) {
            this.cart.put(d,this.cart.get(d)+1);
        } else {
            this.cart.put(d, 1);
        }
    }

}
