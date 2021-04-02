/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grouv.controller;

import grouv.entity.Item;
import grouv.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Geo
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private ProductService productService;

//    @GetMapping
//    public String showCart(Model model) {
//        model.addAttribute("products", productService.findAll());
//        return "cart";
//    }
    @GetMapping
    public String showCart(Model model, HttpSession session) {
        model.addAttribute("total", total(session));
        return "cart";
    }

    @GetMapping(value = "buy/{id}")
    public String buy(@PathVariable("id") int id, HttpSession session) {

        if (session.getAttribute("cart") == null) {
            List<Item> items = new ArrayList();
            items.add(new Item(productService.find(id), 1, 1));
            session.setAttribute("cart", items);
        } else {
            List<Item> items = (List<Item>) session.getAttribute("cart");
            items.add(new Item(productService.find(id), 1, 1));
//            int index = checkIfExists(id, items);
//            if (index == -1) {
//                items.add(new Item(productService.find(id), 1, 1));
//            } else {
//            //check if quality exists 
//                int quantity = items.get(index).getQuantity() + 1;
//                items.get(index).setQuantity(quantity);
//            }
        }
        return "redirect:/cart";
    }

    @PostMapping(value = "update")
    public String update(HttpServletRequest request, HttpSession session) {

        String[] quantities = request.getParameterValues("quantity");
        String[] qualities = request.getParameterValues("quality");
        List<Item> items = (List<Item>) session.getAttribute("cart");
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setQuantity(Integer.parseInt(quantities[i]));
            items.get(i).setQuality(Float.parseFloat(qualities[i]));
        }
        onlyOne(session); //unifies multiple entries of the same product and quality
        session.setAttribute("cart", items);
        return "redirect:/cart";
    }

    @GetMapping(value = "delete/{id}/{quality}/{quantity}")
    public String delete(@PathVariable("id") int id, @PathVariable("quality") float quality,
            @PathVariable("quantity") int quantity, HttpSession session) {

        List<Item> items = (List<Item>) session.getAttribute("cart");
        int index = checkIfExists(id, quality, quantity, items);
        items.remove(index);
        session.setAttribute("cart", items);
        return "redirect:/cart";
    }

//    private int checkIfExists(int id, List<Item> items) {
//        for (int i = 0; i < items.size(); i++) {
//            if (items.get(i).getProduct().getId() == id) {
//                return i;
//            }
//        }
//        return -1;
//    }
    private int checkIfExists(int id, float quality, int quantity, List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId() == id) {
                if (items.get(i).getQuality() == quality) {
                    if (items.get(i).getQuantity() == quantity) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private java.math.BigDecimal total(HttpSession session) {
        List<Item> items = (List<Item>) session.getAttribute("cart");
        java.math.BigDecimal s = java.math.BigDecimal.valueOf(0);
        for (int i = 0; i < items.size(); i++) {
            s = s.add(java.math.BigDecimal.valueOf((items.get(i).getQuantity() * items.get(i).getQuality())).multiply(items.get(i).getProduct().getBasePrice()));
        }
        session.setAttribute("cartvalue", s); //saves the cart's sum to an attribute named cartvalue
        return s;
    }

    private void onlyOne(HttpSession session) {
        List<Item> items = (List<Item>) session.getAttribute("cart");
        if (items.size() > 1) {
            for (int i = 0; i < items.size(); i++) {
                for (int j = 1; j < (items.size() - i); j++) {
                    if (items.get(i).getProduct().getId() == items.get(i + j).getProduct().getId()) {
                        if (items.get(i).getQuality() == items.get(i + j).getQuality()) {
                            items.get(i).setQuantity(items.get(i).getQuantity() + items.get(i+j).getQuantity());
                            items.remove(i + j);
                        }
                    }
                }
            }
        }
    }

    @GetMapping(value = "/checkout")
    public String checkout(HttpSession session) {
        return "checkout";
    }

}
