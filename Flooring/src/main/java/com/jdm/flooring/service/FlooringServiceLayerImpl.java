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
    
    private static final String VALID_DATE_REGEX = "^(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])-[0-9]{4}$";
    private static final String VALID_NAME_REGEX = "^[A-Za-z\\s,.`]+$";
    private static final String VALID_STATE_REGEX = "^[A-Z][A-Z]+$";
    private static final String VALID_AREA_REGEX = "^([0-9]+\\.?[0-9]*|\\.[0-9]+)$";
    
    public FlooringServiceLayerImpl(FlooringDao dao, FlooringAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }
    
    @Override
    public List<Order> getOrdersByDate(String date) throws InvalidInputException{
        
        //Validate date format
        if(!date.matches(VALID_DATE_REGEX)) throw new InvalidInputException("Invalid date format");
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
                            throws DateAlreadyPassedException, InvalidInputException, TaxCodeViolationException{
        
        BigDecimal area;
        //Convert string to BigDecimal with input validation
        if(!areaStr.matches(VALID_AREA_REGEX)){
            throw new InvalidInputException("Invalid input for area.");
        }
        else{
            area = new BigDecimal(areaStr);
        }
        
        //Validate date format
        if(!date.matches(VALID_DATE_REGEX)) {
            throw new InvalidInputException("Invalid date format");
        }
        //Input validation for customer name (not blank, only valid characters)
        else if(!customerName.matches(VALID_NAME_REGEX)){
            throw new InvalidInputException("The name entered contains invalid characters or is an empty field.");
        }
        //Verify date is in the future
        else if(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy")).isBefore(LocalDate.now())){
            throw new DateAlreadyPassedException("The date entered is before today's date.");
        }
        else if(!state.matches(VALID_STATE_REGEX)){
            throw new InvalidInputException("The state was not entered properly.");
        }
        //Verify state exists in tax file
        else if(!dao.checkTaxCode(state)){
            throw new TaxCodeViolationException("We cannot sell products in your state.");
        }
        //Verify areaStr is a positive decimal value
        else if(area.compareTo(new BigDecimal("0")) <= 0){
            throw new InvalidInputException("The area value entered is not a positive value.");
        }
        //Verify the productType is a valid product 
        else if(!dao.checkProductType(productType)){
            throw new InvalidInputException("The product type entered does not exist.");
        }
        //everything is valid, create object and return it so a summary can be shown
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

    @Override
    public Order getOrderToEdit(String date, String customerName) throws InvalidInputException, NoSuchItemException{
        //Validate date format
        if(!date.matches(VALID_DATE_REGEX)) {
            throw new InvalidInputException("Invalid date format.");
        }
        //Input validation for customer customerName (not blank, only valid characters)
        else if(!customerName.matches(VALID_NAME_REGEX)){
            throw new InvalidInputException("The name entered contains invalid characters or is an empty field.");
        }
        
        Order order = dao.getOrderByNameDate(date, customerName);
        
        //Does the order exist
        if(order == null){
            throw new NoSuchItemException("There is not an order on " + date + " under the name " + customerName + ".");
        }
        else{
            return order;
        }
    }

    @Override
    public Order editOrder(Order order, String customerName, String state, String productType, String areaStr) throws InvalidInputException, TaxCodeViolationException {
        BigDecimal area;
        
        //Convert string to BigDecimal
        if(!areaStr.equals("")){
            area = new BigDecimal(areaStr); 
        }
        else if (!areaStr.matches(VALID_AREA_REGEX)){
            throw new InvalidInputException("Invalid input for area.");
        }
        //Placeholder BigDecimal value for empty areaStr
        else{ 
            area = new BigDecimal("1");
        }
        
        //Input validation for customerName (not blank, only valid characters)
        if(!customerName.equals("") && !customerName.matches(VALID_NAME_REGEX)){
            throw new InvalidInputException("The name entered contains invalid characters or is an empty field.");
        }
        else if(!state.equals("") && !state.matches(VALID_STATE_REGEX)){
            throw new InvalidInputException("The state was not entered properly.");
        }
        //Verify state exists in tax file
        else if(!state.equals("") && !dao.checkTaxCode(state)){
            throw new TaxCodeViolationException("We cannot sell products in your state.");
        }
        //Verify areaStr is a positive decimal value
        else if(!areaStr.equals("") && area.compareTo(new BigDecimal("0")) <= 0){
            throw new InvalidInputException("The area value entered is not a positive value.");
        }
        //Verify the productType is a valid product 
        else if(!productType.equals("") && !dao.checkProductType(productType)){
            throw new InvalidInputException("The product type entered does not exist.");
        }
        
        else{
            if(!areaStr.equals("")){
                order.setArea(area);
            }
            if(!state.equals("")){
                order.setState(state);
            }
            if(!productType.equals("")){
                order.setProductType(productType);
            }
            if (!customerName.equals("")){
                order.setCustomerName(customerName);
            }
            dao.recalculateOrder(order);
        }
        return order;
    }

    @Override
    public void changeOrder(Order order) {
        dao.updateOrder(order);
    }

}
