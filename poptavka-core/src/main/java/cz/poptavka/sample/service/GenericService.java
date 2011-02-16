
package cz.poptavka.sample.service;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.common.DomainObject;

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

}
