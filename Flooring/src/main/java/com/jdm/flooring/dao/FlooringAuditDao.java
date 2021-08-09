package com.jdm.flooring.dao;

/**
 *
 * @author Joe
 */
public interface FlooringAuditDao {
    
    /**
     * Appends parameter entry to audit file.
     * @param entry
     * @throws FlooringDaoException
     */
    public void writeAuditEntry(String entry) throws FlooringDaoException;
}
