package com.jdm.flooring.service;

import com.jdm.flooring.dao.FlooringAuditDao;
import com.jdm.flooring.dao.FlooringDao;
import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.dto.Order;
import com.jdm.flooring.dto.Product;
import java.math.BigDecimal;
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
    public Order createOrder(String date, String customerName, String state, String productType, String areaStr) 
                            throws DateAlreadyPassedException, InvalidInputException, TaxCodeViolationException, DateFormatException{
        BigDecimal area = new BigDecimal(areaStr); 
        //VALIDATE DATE FORMAT
        String dateFormatRegex = "^(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])-[0-9]{4}$";
        
        if(!date.matches(dateFormatRegex)) {
            throw new DateFormatException("Invalid date format");
        }
        //Verify date is in the future
        else if(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy")).isBefore(LocalDate.now())){
            throw new DateAlreadyPassedException("The date entered is before today's date.");
        }
        //Input validation for customer name (not blank, only valid characters)
        else if(!customerName.matches("^[A-Za-z\\s,.`]+$")){
            throw new InvalidInputException("The name entered contains invalid characters or is an empty field.");
        }
        //Verify state exists in tax file
        else if(!dao.checkTaxCode(state)){
            throw new TaxCodeViolationException("We cannot sell products in your state.");
        }
        //Verify areaStr is a positive decimal value
        else if(area.compareTo(new BigDecimal("0")) <= 0){
            throw new InvalidInputException("The area value entered is not a positive value.");
        }
        else if(!dao.checkProductType(productType)){
            throw new InvalidInputException("The product type you entered does not exist.");
        }
        //create object and return it so a summary can be shown
        else{
            return dao.createOrder(date, customerName, state, productType, area);
        }
    }
    
    @Override
    public void submitOrder(Order order){
        dao.addOrder(order);
    }

    @Override
    public List<Product> getProducts() {
        return dao.getProducts();
    }

}
