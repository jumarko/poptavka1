package cz.poptavka.sample.dao;


import cz.poptavka.sample.domain.common.DomainObject;

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
     * flushes the cachce to the database.
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
     * Runs named query which should return single result.
     *
     * @param name   of the query
     * @param params
     * @return
     */
    Object runNamedQueryForSingleResult(String name, Map<String, Object> params);

    /**
     * Returns the type of the implemented Dao.
     *
     * @return
     */
    Class<? extends T> getPersistentClass();
}
