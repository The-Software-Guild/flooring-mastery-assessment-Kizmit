/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jdm.flooring.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Joe McAdams
 * @email joedmcadams@gmail.com
 * 
 */
public class Order {
    private String orderNumber, customerName, state, productType;
    private BigDecimal taxRate, costPerSqFt, laborCostPerSqFt, area, materialCost, laborCost, taxCost, total;
    private LocalDate orderDate;
    
    //Constructor for add order
    public Order(String customerName, String state, String productType, LocalDate orderDate, BigDecimal area) {
        this.customerName = customerName;
        this.state = state;
        this.productType = productType;
        this.orderDate = orderDate;
        this.area = area;
        setCalculableValues();
    }
    
    private void setCalculableValues() {
        this.materialCost = area.multiply(costPerSqFt);
        this.laborCost = area.multiply(laborCostPerSqFt);
        BigDecimal subTotal = laborCost.add(materialCost);
        this.taxCost = subTotal.multiply((taxRate.divide(new BigDecimal("100"))));
        this.total = subTotal.add(taxCost);
    }
    
    //Constructor for file import
    public Order(String orderNumber, String customerName, String state, BigDecimal taxRate, 
            String productType, BigDecimal area, BigDecimal costPerSqFt, BigDecimal laborCostPerSqFt, 
            BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxCost, 
            BigDecimal total) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.productType = productType;
        this.taxRate = taxRate;
        this.costPerSqFt = costPerSqFt;
        this.laborCostPerSqFt = laborCostPerSqFt;
        this.area = area;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.taxCost = taxCost;
        this.total = total;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getCostPerSqFt() {
        return costPerSqFt;
    }

    public void setCostPerSqFt(BigDecimal costPerSqFt) {
        this.costPerSqFt = costPerSqFt;
    }

    public BigDecimal getLaborCostPerSqFt() {
        return laborCostPerSqFt;
    }

    public void setLaborCostPerSqFt(BigDecimal laborCostPerSqFt) {
        this.laborCostPerSqFt = laborCostPerSqFt;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public BigDecimal getTaxCost() {
        return taxCost;
    }

    public BigDecimal getTotal() {
        return total;
    }


    

}
