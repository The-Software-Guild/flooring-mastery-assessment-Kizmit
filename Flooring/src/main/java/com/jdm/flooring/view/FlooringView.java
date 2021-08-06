package com.jdm.flooring.view;

import com.jdm.flooring.dto.Order;
import java.time.LocalDate;
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
        String tableHeader = String.format("%-15s%-10s%-30s%-20s%-45s%-10s", "Order Number",
                "Date", "Customer Name", "State", "Product Type", "Area");
        io.print(tableHeader);
        for(order : )
    }

    public LocalDate getDate() {
        return LocalDate.parse(io.readString("Enter the date of the order(s): yyyy-mm-dd: "));
    }

}
