package cz.poptavka.sample.dao.demand;

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
public final class DemandFilter {

    public static final DemandFilter EMPTY_FILTER = new DemandFilter();

    private FilterOperator filterOperator = FilterOperator.AND;

    private List<Locality> demandLocalities = new ArrayList<Locality>();

    private List<Category> demandCategories = new ArrayList<Category>();

    private DemandFilter() {
        // use builder instead
    }

    //---------------------------------- GETTERS -----------------------------------------------------------------------


    public FilterOperator getFilterOperator() {
        return filterOperator;
    }

    /**
     * Return unmodifiable list of localities to which demands must belong to  satisfy this filter condition.
     * More specifically, demand must belong to given locality or any sublocality of given locality to satisfy filter.
     * @return
     */
    public List<Locality> getDemandLocalities() {
        return Collections.unmodifiableList(demandLocalities);
    }

    public List<Category> getDemandCategories() {
        return Collections.unmodifiableList(demandCategories);
    }

    //----------------------------------  Builder ----------------------------------------------------------------------
    public static final class DemandFilterBuilder {
        private DemandFilter demandFilter;

        private DemandFilterBuilder() {
            demandFilter = new DemandFilter();
        }

        public DemandFilterBuilder withLocalities(List<Locality> localities) {
            this.demandFilter.demandLocalities.addAll(localities);
            return this;
        }

        public DemandFilterBuilder withCategories(List<Category> categories) {
            this.demandFilter.demandCategories.addAll(categories);
            return this;
        }

        public DemandFilterBuilder withOperator(FilterOperator filterOperator) {
            this.demandFilter.filterOperator = filterOperator;
            return this;
        }

        public static DemandFilterBuilder demandFilter() {
            return new DemandFilterBuilder();
        }

        public DemandFilter build() {
            return demandFilter;
        }
    }
}
