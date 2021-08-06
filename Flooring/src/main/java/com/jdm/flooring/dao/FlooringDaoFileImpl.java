/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jdm.flooring.dao;

import com.jdm.flooring.dto.Order;
import com.jdm.flooring.dto.Product;
import com.jdm.flooring.dto.Tax;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author Joe McAdams
 * @email joedmcadams@gmail.com
 * 
 */
public class FlooringDaoFileImpl implements FlooringDao {
    private HashMap<String, Order> orderMap = new HashMap<>();
    private HashMap<String, Product> productMap = new HashMap<>();
    private HashMap<String, Tax> taxMap = new HashMap<>();

    private List<Order> getAllOrders(){
        return new ArrayList<>(orderMap.values());
    }
    
    @Override
    public List<Order> getOrders(LocalDate date) {
        return getAllOrders().stream().filter((order) -> order.getOrderDate() == date)
                .collect(Collectors.toList());
    }
    
    
}
