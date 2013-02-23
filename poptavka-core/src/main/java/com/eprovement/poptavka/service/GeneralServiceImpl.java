package com.eprovement.poptavka.service;

import com.google.common.base.Preconditions;
import com.googlecode.genericdao.dao.jpa.GeneralDAO;
import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.SearchResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 3.5.11
 */
@Transactional
public class GeneralServiceImpl implements GeneralService {

    private GeneralDAO generalDAO;

    public GeneralServiceImpl(GeneralDAO generalDAO) {
        Preconditions.checkArgument(generalDAO != null, "generalDAO cannot be null!");
        this.generalDAO = generalDAO;
    }

    @Override
    public <T> T find(Class<T> type, Serializable id) {
        return this.generalDAO.find(type, id);
    }

    @Override
    public <T> T[] find(Class<T> type, Serializable... ids) {
        return this.generalDAO.find(type, ids);
    }

    @Override
    public <T> T getReference(Class<T> type, Serializable id) {
        return this.generalDAO.getReference(type, id);
    }

    @Override
    public <T> T[] getReferences(Class<T> type, Serializable... ids) {
        return this.generalDAO.getReferences(type, ids);
    }

    @Override
    public void persist(Object... entities) {
        this.generalDAO.persist(entities);
    }

    @Override
    public <T> T merge(T entity) {
        return this.generalDAO.merge(entity);
    }

    @Override
    public Object[] merge(Object... entities) {
        return this.generalDAO.merge(entities);
    }

    @Override
    public <T> T save(T entity) {
        return this.generalDAO.save(entity);
    }

    @Override
    public Object[] save(Object... entities) {
        return this.generalDAO.save(entities);
    }

    @Override
    public boolean remove(Object entity) {
        return this.generalDAO.remove(entity);
    }

    @Override
    public void remove(Object... entities) {
        this.generalDAO.remove(entities);
    }

    @Override
    public boolean removeById(Class<?> type, Serializable id) {
        return this.generalDAO.removeById(type, id);
    }

    @Override
    public void removeByIds(Class<?> type, Serializable... ids) {
        this.generalDAO.removeByIds(type, ids);
    }

    @Override
    public <T> List<T> findAll(Class<T> type) {
        return this.generalDAO.findAll(type);
    }

    @Override
    public List search(ISearch search) {
        return this.generalDAO.search(search);
    }

    @Override
    public Object searchUnique(ISearch search) {
        return this.generalDAO.searchUnique(search);
    }

    @Override
    public int count(ISearch search) {
        return this.generalDAO.count(search);
    }

    @Override
    public SearchResult searchAndCount(ISearch search) {
        return this.generalDAO.searchAndCount(search);
    }

    @Override
    public boolean isAttached(Object entity) {
        return this.generalDAO.isAttached(entity);
    }

    @Override
    public void refresh(Object... entities) {
        this.generalDAO.refresh(entities);
    }

    @Override
    public void flush() {
        this.generalDAO.flush();
    }

    @Override
    public Filter getFilterFromExample(Object example) {
        return this.generalDAO.getFilterFromExample(example);
    }

    @Override
    public Filter getFilterFromExample(Object example, ExampleOptions options) {
        return this.generalDAO.getFilterFromExample(example, options);
    }
}
