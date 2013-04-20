package com.eprovement.poptavka.application.persistence;

import com.eprovement.poptavka.domain.common.DomainObject;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect for automatic enabling of Hibernate filters to all sessions.
 * This way we can add common filter conditions to all entities, such as filtering by
 * {@link com.eprovement.poptavka.domain.common.DomainObject#enabled} flag.
 */
@Aspect
public class EnableFilterAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnableFilterAspect.class);

    /**
     * Pointcut matches external hibernate-core library - load-time weaving has to be used.
     */
    @Pointcut("execution(* org.hibernate.SessionBuilder+.openSession(..))")
    private void openSessionPointcut() {
    }

    @AfterReturning(pointcut = "openSessionPointcut()", returning = "session")
    public void turnOnEnabledFilter(Session session) {
        turnOnFilter(session, DomainObject.ENABLED_FILTER_NAME);
    }

    private void turnOnFilter(Session session, String filterName) {
        if (session == null) {
            LOGGER.warn("No sessions defined. filterName={} will not be applied.", filterName);
            return;
        }
        if (session.getEnabledFilter(filterName) == null) {
            session.enableFilter(filterName);
        }
    }
}
