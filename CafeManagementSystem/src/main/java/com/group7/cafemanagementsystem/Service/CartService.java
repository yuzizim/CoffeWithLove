package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.Cart;

import java.util.List;

public interface CartService {
    Cart addItemToCart(int id, String username, int quantity);

    Cart updateQuantity(int id, int quantity);

    List<Cart> getCartByUser(String username);

    void deleteItemFromCart(int foodId);

    boolean checkItemExistInCart(int foodId);
}
