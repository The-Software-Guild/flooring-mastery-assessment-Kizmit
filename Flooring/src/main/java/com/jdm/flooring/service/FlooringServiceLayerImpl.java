package com.jdm.flooring.service;

import com.jdm.flooring.dao.FlooringAuditDao;
import com.jdm.flooring.dao.FlooringDao;
import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.dto.Order;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public FlooringServiceLayerImpl(FlooringDao dao, FlooringAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }
    
    @Override
    public List<Order> getOrdersByDate(String date) throws DateFormatException{
        //VALIDATE DATE FORMAT
        String dateFormatRegex = "^(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])-[0-9]{4}$";
        
        if(!date.matches(dateFormatRegex)) throw new DateFormatException("Invalid date format");
        else{
            return dao.getOrdersByDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        }
    }

    @Override
    public void importAllData() throws FlooringDaoException {
        try{
            dao.importOrderData();
            dao.importProductData();
            dao.importTaxData();
        }
        catch(FlooringDaoException e){
            throw new FlooringDaoException(e.getMessage());
        }
        
    }

    @Override
    public Order addOrder(String date, String customerName, String state, String productType, String area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
