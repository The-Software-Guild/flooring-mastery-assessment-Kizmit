/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jdm.flooring.dto;

import java.math.BigDecimal;

/**
 *
 * @author Joe McAdams
 * @email joedmcadams@gmail.com
 * 
 */
public class Tax {
    private final String stateAbbrev, stateName;
    private final BigDecimal taxRate;

    public Tax(String stateAbbrev, String stateName, BigDecimal taxRate) {
        this.stateAbbrev = stateAbbrev;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }
    
    public String getStateAbbrev() {
        return stateAbbrev;
    }

    public String getStateName() {
        return stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

}
