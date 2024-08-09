package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.Cart;

import java.util.List;

public interface CartService {
    Cart addItemToCart(int id, String username, int quantity);

    Cart updateQuantity(int id, int quantity, String userName);

    List<Cart> getCartByUser(String username);

    void deleteItemFromCart(int foodId, String userName);

    boolean checkItemExistInCart(int foodId, String userName);
}
