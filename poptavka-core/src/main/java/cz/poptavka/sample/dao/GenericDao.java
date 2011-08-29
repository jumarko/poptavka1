package cz.poptavka.sample.dao;


import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.common.ResultCriteria;
import org.hibernate.criterion.Example;

import java.util.List;
import java.util.Map;

public interface GenericDao<T extends DomainObject> {

    /**
     * Finds the object by its id.
     *
     * @param id   of the object
     * @param lock if the object should be locked in the database
     * @return
     */
    T findById(long id, boolean lock);


    /**
     * Finds the object by its id.
     *
     * @param id of the object
     * @return
     */
    T findById(long id);

    /**
     * Returns list of all entities in natural sort order.
     *
     * @return
     */
    List<T> findAll();

    /**
     * Load all entities that satisfy additional criteria - this could be limit on max number of result and
     * similar restrictions.
     * <p>
     *     For more information:
     *     @see cz.poptavka.sample.domain.common.ResultCriteria
     * @return all entities that resultCriteria
     */
    List<T> findAll(ResultCriteria resultCriteria);

    /**
     * Gets the count of all the records of the associated entity
     * @return count of the records
     */
    long getCount();

    /**
     * Searches for the objects by their respective id's.
     * <p/>
     * This method follows it's common sense.
     *
     * @param Ids
     * @param lck
     * @return objects
     */
    List<T> findByIds(long[] ids, boolean lck);

    /**
     * Merge the state of the given entity into the current dao context.
     *
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     * Save the state entity or updated it).
     * @param entity
     * @return
     */
    T saveOrUpdate(final T entity);

    /**
     * Update the persistent instance with the identifier of the given detached instance.
     * If there is a persistent instance with the same identifier, an exception is thrown.
     * This operation cascades to associated instances if the association is mapped with cascade="save-update".
     *
     * @param entity a detached instance containing updated state
     * @see org.hibernate.Session#update(Object)
     */
    T update(T entity);

    /**
     * Persists the entity.
     *
     * @param entity
     */
    void create(T entity);

    /**
     * Returns true if the entity is persisted.
     *
     * @param entity
     * @return
     */
    boolean contains(T entity);

    /**
     * Checks if entity with given id is persisted.
     *
     * @param id
     * @return true if persisted, false otherwise
     */
    boolean containsById(Long id);

    /**
     * Deletes the entity from the database.
     *
     * @param entity
     */
    void delete(T entity);

    /**
     * Refreshes the entity from the database.
     *
     * @param entity
     * @return
     */
    <T> T refresh(T entity);

    /**
     * flushes the cache to the database.
     */
    void flush();

    /**
     * Runs named query.
     *
     * @param name   of the query
     * @param params can be null. In that case no parameters are set.
     * @return
     */
    List runNamedQuery(String name, Map<String, Object> params);

    /**
     * Runs a named query with additional criteria
     *
     * @param name           of the query
     * @param params         can be null. In that case no parameters are set.
     * @param resultCriteria additional result criteria
     * @return
     */
    List runNamedQuery(String name, Map<String, Object> params,
            ResultCriteria resultCriteria);

    /**
     * Runs named query which should return single result.
     *
     * @param name   of the query
     * @param params
     * @return
     */
    Object runNamedQueryForSingleResult(String name, Map<String, Object> params);

    /**
     * Runs named query which should return single result and applies additional
     * criteria to it.
     *
     * @param name           of the query
     * @param params
     * @param resultCriteria
     * @return
     */
    Object runNamedQueryForSingleResult(String name,
            Map<String, Object> params, ResultCriteria resultCriteria);

    /**
     * Returns the type of the implemented Dao.
     *
     * @return
     */
    Class<? extends T> getPersistentClass();

    /**
     *  Load all entities that satisfy conditions given by <code>example</code> also known as "Query by Example".
     *  <p>
     *  The default setting is that NULL and ZERO values are excluded! If you want to bypass these restrictions, you
     *  must use the more general method {@link #findByExampleCustom(org.hibernate.criterion.Example)}.
     *
     * @param example "entity filter"
     * @return list of all entities satisfying given criteria
     * @throws IllegalArgumentException if given <code>example</code> object is null
     */
    List<T> findByExample(T example);

     /**
     * The same as {@link #findByExample(cz.poptavka.sample.domain.common.DomainObject)}
     * but additonal criteria are applied on the result.
     *
     * <p>
     * See {@link #getAll(cz.poptavka.sample.domain.common.ResultCriteria)}
     *
     * @param example
     * @param resultCriteria
     * @return
     */
    List<T> findByExample(T example, ResultCriteria resultCriteria);

    /**
     * This method similar to {@link #findByExample(cz.poptavka.sample.domain.common.DomainObject)} but allows custom
     * specification of custom Example.
     * <p>
     * This allows you to use enable (e.g.) NULL values or ZERO values.
     * For more information see {@link org.hibernate.criterion.Example}
     * <p>
     *     Be aware when using this! It's quite easy to break the backward compatibility if you use this method
     * to allow (e.g.) NULL values. If new property is added on given domain object (<code>example</code>) then
     * existing code might be broken.
     *
     * @param customExample example which will be used to create a Criteria Query.
     * @return list of all domain objects that satisfy criteria given by <code>customExample</code>.
     */
    List<T> findByExampleCustom(Example customExample);

    /**
     * The same as {@link #findByExampleCustom(org.hibernate.criterion.Example)}
     * but additonal criteria are applied on the result.
     *
     * <p>
     * See {@link #getAll(cz.poptavka.sample.domain.common.ResultCriteria)}
     *
     * @param customExample
     * @param resultCriteria
     * @return
     */
    List<T> findByExampleCustom(Example customExample, ResultCriteria resultCriteria);

}
