package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.Cart;

import java.util.List;

public interface CartService {
    Cart addItemToCart(int id, String username);

    Cart updateQuantity(int id, int quantity);

    List<Cart> getCartByUser(String username);
}
