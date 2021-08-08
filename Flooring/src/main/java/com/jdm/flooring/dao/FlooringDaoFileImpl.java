package com.jdm.flooring.dao;

import com.jdm.flooring.dto.Order;
import com.jdm.flooring.dto.Product;
import com.jdm.flooring.dto.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 *
 * @author Joe McAdams
 * @email joedmcadams@gmail.com
 * 
 */
public class FlooringDaoFileImpl implements FlooringDao {
    private HashMap<String, Order> ordersMap = new HashMap<>();
    private HashMap<String, Product> productMap = new HashMap<>();
    private HashMap<String, Tax> taxMap = new HashMap<>();
    private final String PRODUCT_FILE, TAX_FILE, ORDER_FILE_PREFIX;
    private static final String DELIMITER = "::";
    
    public FlooringDaoFileImpl(String orderFile, String productFile, String taxFile){
        this.ORDER_FILE_PREFIX = "Orders_";
        this.PRODUCT_FILE = productFile;
        this.TAX_FILE = taxFile;
    }
    
    public List<Order> getAllOrders(){
        return new ArrayList<>(ordersMap.values());
    }
    
    @Override
    public List<Order> getOrdersByDate(LocalDate date) {
        return getAllOrders().stream().filter((order) -> order.getOrderDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public void importOrderData() throws FlooringDaoException{
        Scanner scanner = null;
        File ordersDir = new File("Orders");
        String currentLine;
        Order order;
        try{
            for(File ordersFile : ordersDir.listFiles()){
                scanner = new Scanner(new BufferedReader(new FileReader(ordersFile)));
                scanner.nextLine(); //Ignore header line
                while(scanner.hasNextLine()){
                    currentLine = scanner.nextLine();
                    order = unmarshallOrder(currentLine);
                    String dateString = ordersFile.getName().replace(ORDER_FILE_PREFIX, "");
                    dateString = dateString.replace(".txt", "");
                    LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMddyyyy"));
                    date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    order.setOrderDate(date);
                    ordersMap.put(order.getOrderNumber(), order);
                }
            }  
        }
        catch(FileNotFoundException e){
            throw new FlooringDaoException("Couldn't read order files");
        }
        scanner.close();
    }

    private Order unmarshallOrder(String orderAsText){
        String orderTokens[] = orderAsText.split(DELIMITER);
        Order orderFromFile = new Order(orderTokens[0], orderTokens[1], orderTokens[2], new BigDecimal(orderTokens[3]),
                orderTokens[4],new BigDecimal(orderTokens[5]), new BigDecimal(orderTokens[6]), new BigDecimal(orderTokens[7]),
                new BigDecimal(orderTokens[8]), new BigDecimal(orderTokens[9]),new BigDecimal(orderTokens[10]),
                new BigDecimal(orderTokens[11]));
        return orderFromFile;
    }
    
    @Override
    public void importProductData() throws FlooringDaoException {
        Scanner scanner = null;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        }
        catch(FileNotFoundException e){
            throw new FlooringDaoException("Couldn't read product file");
        }
        
        String currentLine;
        Product product;
        
        scanner.nextLine(); //Ignore header line
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            product = unmarshallProduct(currentLine);
            productMap.put(product.getProductType(), product);
        }
        scanner.close();
    }
    
    private Product unmarshallProduct(String productAsText) {
        String productTokens[] = productAsText.split(DELIMITER);
        Product productFromFile = new Product(productTokens[0], new BigDecimal(productTokens[1]), 
                new BigDecimal(productTokens[2]));
        return productFromFile;
    }
    
    @Override
    public void importTaxData() throws FlooringDaoException {
        Scanner scanner = null;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        }
        catch(FileNotFoundException e){
            throw new FlooringDaoException("Couldn't read tax file");
        }
        
        String currentLine;
        Tax tax;
        
        scanner.nextLine(); //Ignore header line
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            tax = unmarshallTax(currentLine);
            taxMap.put(tax.getStateAbbrev(), tax);
        }
        
        scanner.close();
    }

    private Tax unmarshallTax(String taxAsText) {
        String[] taxTokens = taxAsText.split(DELIMITER);
        Tax taxFromFile = new Tax(taxTokens[0], taxTokens[1], new BigDecimal(taxTokens[2]));
        return taxFromFile;
    }

    @Override
    public String addOrder(Order newOrder) {
        //Get the highest order number and create a new one based off of it (by adding 1)
        Order order = getAllOrders().stream().max(Comparator.comparing(var -> var.getOrderNumber())).get();
        int orderNumberInt = Integer.parseInt(order.getOrderNumber()) + 1;
        String newOrderNumber = String.valueOf(orderNumberInt);
        
        //Assign the new order number to a newly placed order and add the order to the map
        newOrder.setOrderNumber(newOrderNumber);
        ordersMap.put(newOrder.getOrderNumber(), newOrder);
        
        //Return the newly generated order number to display
        return newOrderNumber;
    }

    @Override
    public Order createOrder(String date, String customerName, String state, String productType, BigDecimal area) {
        return new Order(customerName, state, productType, LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy")), area, 
                taxMap.get(state).getTaxRate(), productMap.get(productType).getCostPerSqFt(), productMap.get(productType).getLaborCostPerSqFt());
    }

    @Override
    public boolean checkTaxCode(String state) {
        return taxMap.containsKey(state);
    }

    @Override
    public List<Product> getProducts() {
        return new ArrayList<>(productMap.values());
    }
    
    @Override
    public boolean checkProductType(String productType){
        return productMap.containsKey(productType);
    }

    @Override
    public Order getOrderByNameDate(String date, String customerName) {
        List<Order> orderList = getAllOrders().stream().filter((ord) -> ord.getOrderDate().equals(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy"))) 
                && ord.getCustomerName().equals(customerName)).collect(Collectors.toList());
        Order order = null;
        if(!orderList.isEmpty()){
            order = orderList.get(0);
        }
        return order;
    }

    @Override
    public void updateOrder(Order order) {
        ordersMap.replace(order.getOrderNumber(), order);
    }

    @Override
    public void recalculateOrder(Order order) {
        order.setCostPerSqFt(productMap.get(order.getProductType()).getCostPerSqFt());
        order.setLaborCostPerSqFt(productMap.get(order.getProductType()).getLaborCostPerSqFt());
        order.setTaxRate(taxMap.get(order.getState()).getTaxRate());
        order.recalculate();
    }

    @Override
    public Order getOrderByOrderNumberDate(String orderNumber, String date) {
        if(ordersMap.containsKey(orderNumber) && ordersMap.get(orderNumber).getOrderDate().equals(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy")))){
            return ordersMap.get(orderNumber);
        }
        else{
            return null;
        }
    }

    @Override
    public void removeOrder(Order order) {
        ordersMap.remove(order.getOrderNumber());
    }
}
