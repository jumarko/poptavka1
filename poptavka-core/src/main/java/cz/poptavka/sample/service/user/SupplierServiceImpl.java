package cz.poptavka.sample.service.user;

import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.user.SupplierDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.GenericServiceImpl;

import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class SupplierServiceImpl extends GenericServiceImpl<Supplier, SupplierDao> implements SupplierService {

    private SupplierDao supplierDao;


    /** {@inheritDoc} */
    @Override
    public Set<Supplier> getSuppliers(Locality... localities) {
        return this.supplierDao.getSuppliers(localities);
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "cache5min")
    public long getSuppliersCount(Locality... localities) {
        return this.supplierDao.getSuppliersCount(localities);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Supplier> getSuppliers(Category... categories) {
        return this.supplierDao.getSuppliers(categories);
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "cache5min")
    public long getSuppliersCount(Category... categories) {
        return this.supplierDao.getSuppliersCount(categories);
    }

    //---------------------------------- GETTERS and SETTERS -----------------------------------------------------------

    public void setSupplierDao(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }
}
