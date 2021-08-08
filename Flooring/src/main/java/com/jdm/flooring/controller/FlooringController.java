package com.jdm.flooring.controller;

import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.dto.Order;
import com.jdm.flooring.service.DateAlreadyPassedException;
import com.jdm.flooring.service.FlooringServiceLayer;
import com.jdm.flooring.service.InvalidInputException;
import com.jdm.flooring.service.NoSuchItemException;
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
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
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
        boolean done = false;
        while(!done){
            try{
                view.displayOrders(service.getOrdersByDate(view.getDate()));
                done = true;
            }
            catch(InvalidInputException e){
                view.displayErrorMessage(e.getMessage());
            }
        }
    }

    private void createNewOrder() {
        boolean done = false;
        while(!done){
            try{
                Order order = service.createOrder(view.getDate(), view.getName(), view.getState(), capitalize(view.getProductType(service.getProducts())), view.getArea());
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
                done = true;
            }
            catch(DateAlreadyPassedException | TaxCodeViolationException e){
                view.displayErrorMessage(e.getMessage());
                done = true;
            }
            catch(InvalidInputException e){
                view.displayErrorMessage(e.getMessage());
            }
        }
    }

    private void editOrder() {
        Order order = null;
        boolean done = false;
        
        while(!done){
            try{
                order = service.getOrderToEdit(view.getDate(), view.getName());
                done = true;
            }
            catch(InvalidInputException e){
                view.displayErrorMessage(e.getMessage());
            }
            catch(NoSuchItemException e){
                view.displayErrorMessage(e.getMessage());
                done = true;
            }
        }
        
        done = false;
        while(!done){
            try{
                view.displayOrderEditMessage();
                view.displayOrderSummary(order);
                order = service.editOrder(order, view.getName(), view.getState(), capitalize(view.getProductType(service.getProducts())), view.getArea());
                view.displayOrderSummary(order);
                boolean valid;
                do{
                    String yesNo = view.getOrderConfirmation();
                    if(yesNo.equals("y")) {
                        service.changeOrder(order);
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
                done = true;
            }
            catch(InvalidInputException e){
                view.displayErrorMessage(e.getMessage());
                
            }
            catch(TaxCodeViolationException e){
                view.displayErrorMessage(e.getMessage());
                done = true;
            }
        }
    }

    private void removeOrder() {
        
    }


}
