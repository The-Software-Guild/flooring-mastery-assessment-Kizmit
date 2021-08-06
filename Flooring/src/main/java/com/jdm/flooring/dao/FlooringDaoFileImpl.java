/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jdm.flooring.dao;

import com.jdm.flooring.dto.Order;
import com.jdm.flooring.dto.Product;
import com.jdm.flooring.dto.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private HashMap<String, Order> orderMap = new HashMap<>();
    private HashMap<String, Product> productMap = new HashMap<>();
    private HashMap<String, Tax> taxMap = new HashMap<>();
    private final String ORDERS_FILE, PRODUCT_FILE, TAX_FILE;
    private static final String DELIMITER = "::";
    
    public FlooringDaoFileImpl(String orderFile, String productFile, String taxFile){
        this.ORDERS_FILE = orderFile;
        this.PRODUCT_FILE = productFile;
        this.TAX_FILE = taxFile;
    }
    
    private List<Order> getAllOrders(){
        return new ArrayList<>(orderMap.values());
    }
    
    @Override
    public List<Order> getOrders(LocalDate date) {
        return getAllOrders().stream().filter((order) -> order.getOrderDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public void importBackupData() throws FlooringDaoException{
        Scanner scanner = null;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(ORDERS_FILE)));
        }
        catch(FileNotFoundException e){
            throw new FlooringDaoException("Couldn't read order file");
        }
        
        String currentLine;
        Order order;
        
        scanner.nextLine(); //Ignore header line
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            order = unmarshallBackupOrder(currentLine);
            orderMap.put(order.getOrderNumber(), order);
        }
        
        scanner.close();
    }

    private Order unmarshallBackupOrder(String orderAsText) {
        String orderTokens[] = orderAsText.split(DELIMITER);
        Order orderFromFile = new Order(orderTokens[0], orderTokens[1], orderTokens[2], new BigDecimal(orderTokens[3]),
                orderTokens[4],new BigDecimal(orderTokens[5]), new BigDecimal(orderTokens[6]), new BigDecimal(orderTokens[7]),
                new BigDecimal(orderTokens[8]), new BigDecimal(orderTokens[9]),new BigDecimal(orderTokens[10]),
                new BigDecimal(orderTokens[11]),LocalDate.parse(orderTokens[12], DateTimeFormatter.ofPattern("MM-dd-yyyy")));
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


    
    
}
