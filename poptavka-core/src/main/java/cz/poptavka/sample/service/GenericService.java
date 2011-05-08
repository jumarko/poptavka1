
package cz.poptavka.sample.service;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.common.ResultCriteria;
import org.hibernate.criterion.Example;

import java.util.List;

public interface GenericService<T extends DomainObject, Dao extends GenericDao<T>> {
/**
     * get the entity with given id.
     * @param id
     * @return entity
     * @throws DomainObjectNotFoundException if no object is found
     */
    T getById(long id);

    /**
     * searches the entity with given id.
     * @param id
     * @return entity, null if none is found
     */
    T searchById(long id);

    /**
     * returns all entities.
     * @return
     */
    List<T> getAll();

    /**
     * Load all entities that satisfy additional criteria - this could be limit on max number of result and
     * similar restrictions.
     * <p>
     *     For more information:
     *     @see ResultCriteria
     * @return all entities that resultCriteria
     */
    List<T> getAll(ResultCriteria resultCriteria);

    /**
     * persists the entity.
     * @param entity
     * @return
     */
    T create(T entity);

    /**
     * Updates the entity.
     * @param entity
     * @return
     */
    T update(T entity);

    /**
     * Removes a persisted entity.
     * @param entity
     */
    void remove(T entity);

    /**
     * Refreshes the entity from database.
     * @param entity
     * @return
     */
    T refresh(T entity);

    /**
     * removes entity with given id.
     * @param id
     */
    void removeById(long id);

    Dao getDao();

    void setDao(Dao dao);

    /**
     *  Load all entities that satisfy conditions given by <code>example</code> also known as "Query by Example".
     *  <p>
     *  The default setting is that NULL and ZERO values are excluded! If you want to bypass these restrictions, you
     *  must use the more general method {@link #findByExampleCustom(org.hibernate.criterion.Example)}.
     *  <p>
     *  Only basic types (such as String, int, Date, etc.) are used for filtering. Assocation types are ignored.
     *  (For more information on associated types check the source code of {@link Example}).
     *  Otherwise this method cannot be used and more complicated criteria must be constructed manually.
     *  E.g. following code IS NOT WORKING:
     *  <pre> final Client client = new Client();
        client.setBusinessUserData(
            new BusinessUserData.Builder().personFirstName("Elvira").personLastName("Vytreta").build());
        final List<Client> clientsByNames = this.clientService.findByExample(client);</pre>
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
     * This method similar to {@link #findByExample(cz.poptavka.sample.domain.common.DomainObject)} but allows
     * specification of custom Example.
     * <p>
     * This allows you to enable (e.g.) NULL values or ZERO values. For more information see {@link Example},
     * <p>
     *     Be aware when using this method! It's quite easy to break the backward compatibility if you use it
     * to allow (e.g.) NULL values. If new property is added on given domain object (<code>example</code>) then
     * existing code might be broken.
     *
     * @param customExample example which will be used to create a Criteria Query.
     * @return list of all domain objects that satisfy criteria given by <code>customExample</code>.
     *
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
