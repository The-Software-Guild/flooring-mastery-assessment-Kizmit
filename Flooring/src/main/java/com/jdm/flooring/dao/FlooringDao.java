/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jdm.flooring.dao;

import com.jdm.flooring.dto.Order;
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
}
