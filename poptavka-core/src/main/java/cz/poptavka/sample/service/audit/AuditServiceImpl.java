package cz.poptavka.sample.service.audit;

import cz.poptavka.sample.dao.audit.AuditDao;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
@Transactional(readOnly = true)
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditDao auditDao;

    public AuditServiceImpl(AuditDao auditDao) {
        this.auditDao = auditDao;
    }


    @Override
    public <T> T find(Class<T> cls, Object primaryKey, Number revision)
        throws IllegalArgumentException, NotAuditedException, IllegalStateException {
        return this.auditDao.getAuditReader().find(cls, primaryKey, revision);
    }

    @Override
    public <T> T find(Class<T> cls, String entityName, Object primaryKey, Number revision)
        throws IllegalArgumentException, NotAuditedException, IllegalStateException {
        return this.auditDao.getAuditReader().find(cls, entityName, primaryKey, revision);
    }

    @Override
    public List<Number> getRevisions(Class<?> cls, Object primaryKey)
        throws IllegalArgumentException, NotAuditedException, IllegalStateException {
        return this.auditDao.getAuditReader().getRevisions(cls, primaryKey);
    }

    @Override
    public List<Number> getRevisions(Class<?> cls, String entityName, Object primaryKey)
        throws IllegalArgumentException, NotAuditedException, IllegalStateException {
        return this.auditDao.getAuditReader().getRevisions(cls, entityName, primaryKey);
    }

    @Override
    public Date getRevisionDate(Number revision)
        throws IllegalArgumentException, RevisionDoesNotExistException, IllegalStateException {
        return this.auditDao.getAuditReader().getRevisionDate(revision);
    }

    @Override
    public Number getRevisionNumberForDate(Date date)
        throws IllegalStateException, RevisionDoesNotExistException, IllegalArgumentException {
        return this.auditDao.getAuditReader().getRevisionNumberForDate(date);
    }

    @Override
    public <T> T findRevision(Class<T> revisionEntityClass, Number revision)
        throws IllegalArgumentException, RevisionDoesNotExistException, IllegalStateException {
        return this.auditDao.getAuditReader().findRevision(revisionEntityClass, revision);
    }

    @Override
    public <T> T getCurrentRevision(Class<T> revisionEntityClass, boolean persist) {
        return this.auditDao.getAuditReader().getCurrentRevision(revisionEntityClass, persist);
    }

    // TODO: Make up your mind how the following method could be used
//    @Override
//    public AuditQueryCreator createQuery() {
//        return this.auditDao.getAuditReader().createQuery();
//    }


    @Override
    public boolean isEntityClassAudited(Class<?> entityClass) {
        return this.auditDao.getAuditReader().isEntityClassAudited(entityClass);
    }
}
