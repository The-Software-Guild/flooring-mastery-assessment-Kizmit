package com.jdm.flooring.controller;

import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.dto.Order;
import com.jdm.flooring.service.DateAlreadyPassedException;
import com.jdm.flooring.service.DateFormatException;
import com.jdm.flooring.service.FlooringServiceLayer;
import com.jdm.flooring.service.InvalidInputException;
import com.jdm.flooring.service.TaxCodeViolationException;
import com.jdm.flooring.view.FlooringView;
import static org.springframework.util.StringUtils.capitalize;

/**
 *
 * @author Joe McAdams
 * @email joedmcadams@gmail.com
 * 
 */
public class FlooringController {
    private final FlooringServiceLayer service;
    private final FlooringView view;
    
    public FlooringController(FlooringServiceLayer service, FlooringView view){
        this.service = service;
        this.view = view;
    }
    
    public void run(){
        try{
            service.importAllData();
            
            boolean exit = false;
            while(!exit){
                switch(view.displayGetMenuChoice()){
                    case 1:
                        displayOrdersByDate();
                        break;
                    case 2:
                        createNewOrder();
                        break;
                    case 3:
                        //Edit an order
                    case 4:
                        //Remove an order
                    case 5:
                        //Export data
                    case 6:
                        exit = true;
                        break;
                        //Quit
                }
            }
        }
        catch(FlooringDaoException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void displayOrdersByDate(){
        try{
            view.displayOrders(service.getOrdersByDate(view.getDate()));
        }
        catch(DateFormatException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void createNewOrder() {
        try{
            Order order = service.createOrder(view.getDate(), view.getName(), capitalize(view.getState()), capitalize(view.getProductType(service.getProducts())), view.getArea());
            view.displayOrderSummary(order);
            boolean valid;
            do{
                String yesNo = view.getOrderConfirmation();
                if(yesNo.equals("y")) {
                    service.submitOrder(order);
                    valid = true;
                }
                else if(yesNo.equals("n")){
                    view.displayOrderNotSubmitted();
                    valid = true;
                }
                else{
                    view.displayInvalidChoice();
                    valid =  false;
                }
            }while(!valid);   
        }
        catch(DateAlreadyPassedException | InvalidInputException | DateFormatException | TaxCodeViolationException e){
            view.displayErrorMessage(e.getMessage());
        }
    }
    
}
