/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jdm.flooring.service;

import com.jdm.flooring.dao.FlooringAuditDao;
import com.jdm.flooring.dao.FlooringDaoException;

/**
 *
 * @author Joe McAdams
 * @email joedmcadams@gmail.com
 * 
 */
public class FlooringAuditDaoStubImpl implements FlooringAuditDao {

    @Override
    public void writeAuditEntry(String entry) throws FlooringDaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
