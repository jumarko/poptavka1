package com.eprovement.poptavka.service.fulltext;

import com.eprovement.poptavka.domain.common.DomainObject;

import java.util.List;

/**
 * Basic API for fulltext search queries.
 *
 * @author Juraj Martinka
 *         Date: 20.5.11
 */
public interface FulltextSearchService {

    /**
     * Search for instances of given <code>entityClass</code> by query <code>fulltextQuery</code>
     * but take into account only properties specified by <code>propertyNames</code>.
     *
     * @param entityClass class of persistent entity
     * @param propertyNames fields that we want be searched must be explicitly specified
     * @param fulltextQuery

     *
     * @return all instances of <code>entityClass</code> which match query <code>fulltextQuery</code>
     * on properties <code>propertyNames</code>
     */
    <T extends DomainObject> List<T> search(Class<T> entityClass, String[] propertyNames, String fulltextQuery);


    /**
     * Creates initial fulltext index using properties from configuration file (persistence.xml).
     * <p>
     * This method should be used carefully.
     */
    void createInitialFulltextIndex();
}
