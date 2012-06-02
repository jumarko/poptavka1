package com.eprovement.poptavka.dao.audit;

import org.hibernate.envers.AuditReader;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
public interface AuditDao {
    AuditReader getAuditReader();
}
