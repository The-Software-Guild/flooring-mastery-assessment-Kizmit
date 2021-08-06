package com.jdm.flooring.view;

import com.jdm.flooring.dto.Order;
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
public class FlooringView {
    private UserIO io;
    
    public FlooringView(UserIO io){
        this.io = io;
    }
    
    public int displayGetMenuChoice() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        return io.readInt("Enter a menu option from above: ", 1, 6);
    }

    public void displayOrders(List<Order> orders) {
        String tableHeader = String.format("%-15s%-15s%-30s%-20s%-45s%-10s", "Order Number",
                "Date", "Customer Name", "State", "Product Type", "Area(SqFT)");
        io.print(tableHeader);
        for(Order order : orders){
            String fOrderStr = String.format("%-15s%-15s%-30s%-20s%-45s%-10s", order.getOrderNumber(),
                order.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")), order.getCustomerName(), order.getState(),
                order.getProductType(), order.getArea());
            /*ADD MORE FIELDS TO DISPLAY TO USER ABOUT ORDERS?*/
            io.print(fOrderStr);
        }
    }

    public String getDate() {
        return io.readString("Enter the date of the order(s) (mm-dd-yyyy): ");
    }

    public void displayErrorMessage(String message) {
        io.print(message);
    }
    
    public String getName(){
        return io.readString("Enter your name: ");
    }
    
    public String getState(){
        return io.readString("Enter your state: ");
    }
    
    public String getProductType(){
        return io.readString("Enter the product type you wish to purchase: ");
    }
    
    public BigDecimal getArea(){
        return new BigDecimal(io.readString("Enter the amount you'd like to purchase in square feet: "));
    }
}
