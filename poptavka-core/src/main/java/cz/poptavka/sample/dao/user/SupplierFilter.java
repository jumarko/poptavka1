package cz.poptavka.sample.dao.user;

/**
 * @author Juraj Martinka
 *         Date: 10/7/11
 *         Time: 11:07 PM
 */

import cz.poptavka.sample.dao.common.FilterOperator;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class represents a filter that can be used for applying various restrictions on demands returned
 * from service methods.
 *
 * @see cz.poptavka.sample.service.demand.DemandService
 * @author Juraj Martinka
 *         Date: 12.5.11
 */
public final class SupplierFilter {

    public static final SupplierFilter EMPTY_FILTER = new SupplierFilter();

    private FilterOperator filterOperator = FilterOperator.AND;

    private List<Locality> demandLocalities = new ArrayList<Locality>();

    private List<Category> demandCategories = new ArrayList<Category>();

    private SupplierFilter() {
        // use builder instead
    }

    //---------------------------------- GETTERS -----------------------------------------------------------------------


    public FilterOperator getFilterOperator() {
        return filterOperator;
    }

    /**
     * Return unmodifiable list of localities to which suppliers must belong to  satisfy this filter condition.
     * More specifically, supplier must belong to given locality or any sublocality of given locality to satisfy filter.
     * @return
     */
    public List<Locality> getDemandLocalities() {
        return Collections.unmodifiableList(demandLocalities);
    }

    public List<Category> getDemandCategories() {
        return Collections.unmodifiableList(demandCategories);
    }

    //----------------------------------  Builder ----------------------------------------------------------------------
    public static final class SupplierFilterBuilder {
        private SupplierFilter supplierFilter;

        private SupplierFilterBuilder() {
            supplierFilter = new SupplierFilter();
        }

        public SupplierFilter.SupplierFilterBuilder withLocalities(List<Locality> localities) {
            this.supplierFilter.demandLocalities.addAll(localities);
            return this;
        }

        public SupplierFilter.SupplierFilterBuilder withCategories(List<Category> categories) {
            this.supplierFilter.demandCategories.addAll(categories);
            return this;
        }

        public SupplierFilter.SupplierFilterBuilder withOperator(FilterOperator filterOperator) {
            this.supplierFilter.filterOperator = filterOperator;
            return this;
        }

        public static SupplierFilterBuilder supplierFilter() {
            return new SupplierFilterBuilder();
        }

        public SupplierFilter build() {
            return supplierFilter;
        }
    }
}
