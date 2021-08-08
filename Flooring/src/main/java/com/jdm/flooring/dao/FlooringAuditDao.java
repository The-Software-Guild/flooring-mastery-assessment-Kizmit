package com.jdm.flooring.dao;

/**
 *
 * @author Joe
 */
public interface FlooringAuditDao {
    public void writeAuditEntry(String entry) throws FlooringDaoException;
}
