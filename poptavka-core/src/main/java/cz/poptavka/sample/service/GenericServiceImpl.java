package cz.poptavka.sample.service;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.exception.DomainObjectNotFoundException;
import cz.poptavka.sample.util.collection.GenericComparator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

public class GenericServiceImpl<Dom extends DomainObject, Dao extends GenericDao<Dom>>
        implements GenericService<Dom, Dao> {

    /**
     * dao object.
     */
    private Dao dao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Dom getById(long id) {
        assertConfigured();
        Dom entity = dao.findById(id);
        if (entity == null) {
            throw new DomainObjectNotFoundException(id, dao.getPersistentClass());
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Dom searchById(long id) {
        assertConfigured();
        return dao.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Dom> getAll() {
        assertConfigured();
        return dao.findAll();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Dom> getAll(ResultCriteria resultCriteria) {
        assertConfigured();
        return dao.findAll(resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Dom create(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        dao.create(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Dom update(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        dao.update(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void remove(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        dao.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeById(long id) {
        Dom object = searchById(id);
        if (object != null) {
            remove(object);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Dom refresh(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        return dao.refresh(entity);
    }

    /**
     * Checks whether given additional criteria <code>resultCriteria</code> contains some columns
     * for ordering and eventually applies this ordering.
     *
     * <p>
     *     This implementation use {@link ResultProvider} to avoid calling specific Dao method before
     *     order by criteria are cleared.
     *
     * @param resultProvider provider that is used for lazy loading of result
     * @param orderByCriteria additional criteria from which only the orderBy condition is applied!
     * @return domainObjects ordered by critiera
     */
    protected <T extends DomainObject> Collection<T> applyOrderByCriteria(ResultProvider<T> resultProvider,
                                                                   ResultCriteria orderByCriteria) {
        if (! ResultCriteria.isOrderBySpecified(orderByCriteria)) {
            return resultProvider.getResult();
        }

        // find all properties on domain object by which should order -- LinkedHashSet ensures that
        // priority of order by properties will be saved
        final Set<String> orderByProperties = new LinkedHashSet<String>();
        for (Map.Entry<String, OrderType> orderByEntry : orderByCriteria.getOrderByColumns().entrySet()) {
            final String fieldName = orderByEntry.getKey();
            orderByProperties.add(fieldName);
        }
        // use list not TreeSet, otherwise objects that are considered to be equal will be lost!
        final List<T> orderedDomainObjects = new ArrayList<T>();

        // remove order by from criteria because Dao does not support it!
        // @see GenericDao#applyOrderByCriteria method
        resultProvider.setResultCriteria(new ResultCriteria.Builder()
                .firstResult(orderByCriteria.getFirstResult())
                .maxResults(orderByCriteria.getMaxResults())
                .orderByColumns(Collections.EMPTY_LIST) // order by criteria cleared!
                .build());
        orderedDomainObjects.addAll(resultProvider.getResult());
        // order collection by specified order by properties
        Collections.sort(orderedDomainObjects, new GenericComparator(orderByProperties));
        return orderedDomainObjects;
    }

    /** {@inheritDoc} */
    @Override
    public long getCount() {
        return getDao().getCount();
    }
//
//
//    private void checkPropertyExists(DomainObject domainObject, String fieldName) {
//        Preconditions.checkState(
//                cz.poptavka.sample.util.reflection.ReflectionUtils.hasGetter(domainObject.getClass(), fieldName),
//                "Field with name [" + fieldName + "] does not exist on class [" + domainObject.getClass() + "]!");
//    }


    //----------------------------------  OTHER METHODS ----------------------------------------------------------------
    /**
     * @throws IllegalStateException if dao is not set
     */
    protected void assertConfigured() {
        if (dao == null) {
            throw new IllegalStateException("dao is null");
        }
    }

    /**
     * tries to use dao.
     *
     * @return
     */
    public Dao useDao() {
        assertConfigured();
        return dao;
    }

    @Override
    public Dao getDao() {
        return dao;
    }

    @Override
    public void setDao(Dao dao) {
        this.dao = dao;
    }

}
