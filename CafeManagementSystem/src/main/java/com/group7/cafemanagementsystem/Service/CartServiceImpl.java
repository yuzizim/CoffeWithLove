package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.CartRepository;
import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.Food;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private UserRepository userRepository;
    private FoodService foodService;

    @Override
    public Cart addItemToCart(int id, String username, int quantity) {
        Cart existCart = cartRepository.findByFood_IdAndAccount_UserName(id, username);
        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + quantity);
            return cartRepository.save(existCart);
        }

        Account account = userRepository.findByUserName(username).get();
        Food food = foodService.getFoodById(id);
        Cart cart = new Cart();
        cart.setFood(food);
        cart.setQuantity(quantity);
        cart.setAccount(account);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateQuantity(int id, int quantity, String userName) {
        Cart cart = cartRepository.findByFood_IdAndAccount_UserName(id, userName);
        if (cart.getQuantity() == 1 && quantity == -1) {
            cartRepository.delete(cart);
            return new Cart();
        }
        cart.setQuantity(cart.getQuantity() + quantity);
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartByUser(String username) {
        List<Cart> carts = cartRepository.findByAccount_UserName(username);
        return carts;
    }

    @Override
    public void deleteItemFromCart(int foodId, String userName) {
        cartRepository.deleteByFood_IdAndAccount_UserName(foodId, userName);
    }

    @Override
    public boolean checkItemExistInCart(int foodId, String userName) {
        return cartRepository.findByFood_IdAndAccount_UserName(foodId, userName) != null;
    }
}
