package com.jdm.flooring.service;

import com.jdm.flooring.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Joe
 */
public interface FlooringServiceLayer {

    public List<Order> displayOrders(LocalDate date);
    
}
