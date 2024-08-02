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
    public Cart addItemToCart(int id, String username) {
        Cart existCart = cartRepository.findByFood_Id(id);
        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + 1);
            return cartRepository.save(existCart);
        }

        Account account = userRepository.findByUserName(username).get();
        Food food = foodService.getFoodById(id);
        Cart cart = new Cart();
        cart.setFood(food);
        cart.setQuantity(1);
        cart.setAccount(account);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateQuantity(int id, int quantity) {
        Cart cart = cartRepository.findByFood_Id(id);
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
}
