package com.jdm.flooring.service;

import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Joe
 */
public interface FlooringServiceLayer {

    public List<Order> getOrdersByDate(String date) throws DateFormatException;
    
    public Order addOrder(String date, String customerName, String state, String productType, String area);
    
    public void importAllData() throws FlooringDaoException;
    
}
