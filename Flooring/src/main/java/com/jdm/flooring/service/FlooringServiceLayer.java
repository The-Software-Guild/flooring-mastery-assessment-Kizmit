package com.jdm.flooring.service;

import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.dto.Order;
import com.jdm.flooring.dto.Product;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Joe
 */
public interface FlooringServiceLayer {

    public List<Order> getOrdersByDate(String date) throws DateFormatException;
    
    public Order createOrder(String date, String customerName, String state, String productType, String area)  throws DateAlreadyPassedException, 
            InvalidInputException, TaxCodeViolationException, DateFormatException;
    
    public void submitOrder(Order order);
    
    public List<Product> getProducts();
    
    public void importAllData() throws FlooringDaoException;
    
}
