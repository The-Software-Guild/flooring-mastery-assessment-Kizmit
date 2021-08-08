package com.jdm.flooring.service;

import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.dto.Order;
import com.jdm.flooring.dto.Product;
import java.util.List;

/**
 *
 * @author Joe
 */
public interface FlooringServiceLayer {

    public List<Order> getOrdersByDate(String date) throws InvalidInputException;
    
    public Order createOrder(String date, String customerName, String state, String productType, String area)  throws DateAlreadyPassedException, 
            InvalidInputException, TaxCodeViolationException;
    
    public String submitOrder(Order order);
    
    public List<Product> getProducts();
    
    public void importAllData() throws FlooringDaoException;

    public Order getOrderToEdit(String date, String name) throws InvalidInputException, NoSuchItemException;

    public Order editOrder(Order order, String name, String capitalize, String capitalize0, String area) throws InvalidInputException, TaxCodeViolationException;

    public void changeOrder(Order order) throws FlooringDaoException;

    public Order getOrderToRemove(String date, String orderNumber) throws InvalidInputException, NoSuchItemException;

    public void removeOrder(Order order);

    public void exportAllData() throws FlooringDaoException;

    public void exportBackupData() throws FlooringDaoException;
    
}
