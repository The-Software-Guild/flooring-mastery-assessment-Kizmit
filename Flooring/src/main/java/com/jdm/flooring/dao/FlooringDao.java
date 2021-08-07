/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jdm.flooring.dao;

import com.jdm.flooring.dto.Order;
import com.jdm.flooring.dto.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Joe
 */
public interface FlooringDao {

    public List<Order> getOrdersByDate(LocalDate date);
    
    public void importOrderData() throws FlooringDaoException;
    
    public void importProductData() throws FlooringDaoException;
    
    public void importTaxData() throws FlooringDaoException;
    
    public Order createOrder(String date, String customerName, String state, String productType, BigDecimal area);
    
    public void addOrder(Order newOrder);

    public boolean checkTaxCode(String state);

    public List<Product> getProducts();

    public boolean checkProductType(String productType);

}
