package com.eprovement.poptavka.audit;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.service.user.LoginService;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Hibernate3 interceptor dedicated to manage Auditable entities. It automatically update audit information of auditable
 * entity (i.e. entity which extends {@link DomainObject}})
 * with creation / update date(time) and userId on save or flushDirty.
 *
 * @author Juraj Martinka
 *
 */
@SuppressWarnings("serial")
public class AuditableEntityInterceptor extends EmptyInterceptor {

    private static final String USER_ID_CREATED = "userIdCreated";
    private static final String DATE_CREATED = "insertTime";
    private static final String USER_ID_LAST_UPDATED = "userIdModified";
    private static final String DATE_LAST_UPDATED = "modificationTime";



    @Autowired
    private LoginService loginService;



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        if (entity instanceof DomainObject) {
            return setCreateAuditInformation(currentState, propertyNames)
                    | setUpdateAuditInformation(currentState, propertyNames);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof DomainObject) {
            boolean modified = false;
            modified |= setCreateAuditInformation(state, propertyNames);
            modified |= setUpdateAuditInformation(state, propertyNames);
            return modified;
        }

        return false;
    }

    /**
     * Sets all create audit information of entity through the entity property state and names.
     *
     * @param state current entity property state
     * @param propertyNames entity property names
     * @return true whether the audit information has been set
     */
    protected boolean setCreateAuditInformation(Object[] state, Object[] propertyNames) {
        return setAuditInformation(state, propertyNames, DATE_CREATED, USER_ID_CREATED);
    }

    /**
     * Sets all update audit information of entity through the entity property state and names.
     *
     * @param currentState current entity property state
     * @param propertyNames entity property names
     * @return true whether the audit information has been set
     */
    protected boolean setUpdateAuditInformation(Object[] currentState, Object[] propertyNames) {
        return setAuditInformation(currentState, propertyNames, DATE_LAST_UPDATED, USER_ID_LAST_UPDATED);
    }


    private boolean setAuditInformation(Object[] currentState, Object[] propertyNames, String currentDatePropertyName,
            String userIdPropertyName) {
        boolean modified = false;
        for (int i = 0; i < propertyNames.length; i++) {
            if (userIdPropertyName.equals(propertyNames[i])) {
                currentState[i] = loginService.getLoggedUser().getId();
                modified |= true;
            }

            if (currentDatePropertyName.equals(propertyNames[i])) {
                currentState[i] = getCurrentDate();
                modified |= true;
            }
        }
        return modified;
    }

    /**
     * Returns the user id of current user.
     *
     * @return the user id of current user
     */
    protected Long getUserId() {
        return loginService.getLoggedUser().getId();
    }

    /**
     * Returns actual date.
     *
     * @return actual date
     */
    protected Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

}
