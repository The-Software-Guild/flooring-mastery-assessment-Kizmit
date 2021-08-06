package com.jdm.flooring.controller;

import com.jdm.flooring.dao.FlooringDaoException;
import com.jdm.flooring.service.FlooringServiceLayer;
import com.jdm.flooring.view.FlooringView;

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
        //Load all data into collections from files
        try{
            service.importAllData();
            //Display menu choices and get a choice
            boolean exit = false;
            while(!exit){
                switch(view.displayGetMenuChoice()){
                    case 1:
                        displayOrdersByDate();
                        break;
                    case 2:
                        //Add an order
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
        //Follow through with actions and return to get another menu choice
        
    }

    private void displayOrdersByDate(){
        view.displayOrders(service.displayOrders(view.getDate()));
    }
    
}
