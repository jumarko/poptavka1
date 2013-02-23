package com.eprovement.poptavka.dao.audit;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
public class AuditDaoImpl implements AuditDao {

    private EntityManager entityManager;


    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public AuditReader getAuditReader() {
        return AuditReaderFactory.get(this.entityManager);
    }
}
