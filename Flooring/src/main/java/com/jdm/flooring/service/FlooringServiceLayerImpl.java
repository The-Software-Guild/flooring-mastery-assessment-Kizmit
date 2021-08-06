package com.jdm.flooring.service;

import com.jdm.flooring.dao.FlooringAuditDao;
import com.jdm.flooring.dao.FlooringDao;
import com.jdm.flooring.dto.Order;
import java.time.LocalDate;
import java.util.List;


/**
 *
 * @author Joe McAdams
 * @email joedmcadams@gmail.com
 * 
 */
public class FlooringServiceLayerImpl implements FlooringServiceLayer {
    private FlooringDao dao;
    private FlooringAuditDao auditDao;
    
    @Override
    public List<Order> displayOrders(LocalDate date) {
        //Implement business logic (input validation, existence check)
        return dao.getOrders(date);
    }

}
