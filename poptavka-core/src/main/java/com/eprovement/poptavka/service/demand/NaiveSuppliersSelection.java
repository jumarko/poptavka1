package com.eprovement.poptavka.service.demand;


import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.user.SupplierService;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Juraj Martinka
 *         Date: 9/29/11
 *         Time: 9:34 PM
 */
public class NaiveSuppliersSelection implements SuppliersSelection {

    /** All demand states that are valid for suppliers selection. */
    public static final List<DemandStatus> VALID_DEMAND_STATES =
            Collections.unmodifiableList(Arrays.asList(DemandStatus.ACTIVE, DemandStatus.OFFERED));
    private static final Logger LOGGER = LoggerFactory.getLogger(NaiveSuppliersSelection.class);
    private SupplierService supplierService;

    @Override
    public Set<PotentialSupplier> getPotentialSuppliers(final Demand demand) {
        Preconditions.checkNotNull(demand);
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(demand.getCategories()),
                "Demand must have at least one category assigned.");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(demand.getLocalities()),
                "Demand must have at least one locality assigned.");


        if (! VALID_DEMAND_STATES.contains(demand.getStatus())) {
            LOGGER.debug("action=get_potential_suppliers demand={} is not in valid state, skipping.", demand);
            return Collections.emptySet();
        }

        LOGGER.debug("action=get_potential_suppliers status=start demand={}. Find all possible suppliers for demand",
                demand);
        final Set<Supplier> suppliers = supplierService.getSuppliersIncludingParents(
                demand.getCategories(), demand.getLocalities(), ResultCriteria.EMPTY_CRITERIA);
        LOGGER.debug("action=get_potential_suppliers status=finish demand={} suppliers_count={} ",
                new Object[] {demand, demand.getCategories().size(), suppliers.size()});



        // TODO 1) client city can be used as some indicator
//        demand.getClient().getBusinessUser().getAddresses().get(0).getCity();

        // TODO 2) supplier's company name and description (see BusinessUserData) can be searched through
        // for occurrences of some words from demand's description
//        demand.getDescription();


        // exclude unwanted suppliers
        removeExcludedSuppliers(demand, suppliers);
        removeLowRatingSuppliers(demand, suppliers);
        removeAdminSuppliers(suppliers);


        // convert to set of PotentialSupplier-s  sorted by rating
        final SortedSet<PotentialSupplier> potentialSupplierSet = convertToPotentialSuppliersSet(suppliers);

        // if neccessary - leave out the suppliers with lowest rating
        removeSuppliersBeyondMax(demand, potentialSupplierSet);

        return potentialSupplierSet;
    }


    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }


    /**
     * Check if set <code>potentialSupplierSet</code> contains more than <code>demand#getMaxSuppliers</code> and if so,
     * then remove as many "lowest" (with lowest rating) suppliers as neccessary to satisfy limit.
     *
     * @param demand
     * @param potentialSupplierSet
     */
    private void removeSuppliersBeyondMax(Demand demand, SortedSet<PotentialSupplier> potentialSupplierSet) {
        if (demand.getMaxSuppliers() != null
                && (potentialSupplierSet.size() > demand.getMaxSuppliers())) {

            // number of elements that have to be removed
            final int overflowElementsCount = potentialSupplierSet.size() - demand.getMaxSuppliers();
            LOGGER.debug("action=remove_suppliers_beyond_max status=start demand={} number_of_potential_suppliers={}"
                    + " number_of_suppliers_to_be_removed={}",
                    new Object[] {demand, potentialSupplierSet.size(), overflowElementsCount});

            // number of elements that alread have been removed
            int numberOfRemovedElements = 0;

            for (Iterator<PotentialSupplier> it = potentialSupplierSet.iterator();
                 numberOfRemovedElements < overflowElementsCount;) {
                // process next element
                it.next();
                if (numberOfRemovedElements < overflowElementsCount) {
                    it.remove();
                    numberOfRemovedElements++;
                }
            }

            LOGGER.debug("action=remove_suppliers_beyond_max status=finish demand={} number_of_potential_suppliers={}"
                    + " number_of_suppliers_removed={}",
                    new Object[] {demand, potentialSupplierSet.size(), numberOfRemovedElements});
        }
    }

    private SortedSet<PotentialSupplier> convertToPotentialSuppliersSet(Set<Supplier> suppliers) {
        final SortedSet<PotentialSupplier> potentialSupplierSet = new TreeSet<PotentialSupplier>(
                new Comparator<PotentialSupplier>() {
                    @Override
                    public int compare(PotentialSupplier potentialSupplierOne, PotentialSupplier potentialSupplierTwo) {
                        if (potentialSupplierOne.getRating() != potentialSupplierTwo.getRating()) {
                            return potentialSupplierOne.getRating() - potentialSupplierTwo.getRating();
                        }

                        if (potentialSupplierOne.getSupplier().getOveralRating() != null) {
                            if (potentialSupplierTwo.getSupplier().getOveralRating() == null) {
                                // since second supplier has no overall rating, the first one is always more preferred
                                return 1;
                            }
                            if (potentialSupplierOne.getSupplier().getOveralRating()
                                    .compareTo(potentialSupplierTwo.getSupplier().getOveralRating()) != 0) {

                                return potentialSupplierOne.getSupplier().getOveralRating()
                                        .compareTo(potentialSupplierTwo.getSupplier().getOveralRating());
                            }
                        } else if (potentialSupplierTwo.getSupplier().getOveralRating() != null) {
                            // supplier with rating should be preferred before supplier without rating
                            return -1;
                        }


                        // both suppliers have completely equal ratings, it does not matter in what order they appears
                        // in sorted set, but they must be there! (therefore we cannot return 0)
                        return 1;
                    }
                }
        );
        for (Supplier supplier : suppliers) {
            potentialSupplierSet.add(new PotentialSupplier(supplier));
        }
        return potentialSupplierSet;
    }

    private void removeLowRatingSuppliers(final Demand demand, Set<Supplier> suppliers) {
        if (demand.getMinRating() != null) {
            int suppliersNumber = suppliers.size();
            LOGGER.debug("action=remove_low_rating_suppliers status=start demand={} original_suppliers_count={}",
                    demand, suppliersNumber);
            CollectionUtils.filter(suppliers, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    final Integer supplierOveralRating = ((Supplier) o).getOveralRating();
                    return supplierOveralRating == null
                            || sufficientRating(supplierOveralRating, demand);

                }
            });
            LOGGER.debug("action=remove_low_rating_suppliers status=finish demand={} number_of_low_rating_suppliers={}",
                    demand, suppliersNumber - suppliers.size());

        }
    }

    private void removeExcludedSuppliers(final Demand demand, Set<Supplier> suppliers) {
        if (CollectionUtils.isNotEmpty(demand.getExcludedSuppliers())) {
            LOGGER.debug("action=remove_excluded_suppliers status=start number_of_excluded_suppliers={}",
                    demand.getExcludedSuppliers().size());
            CollectionUtils.filter(suppliers, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ! demand.getExcludedSuppliers().contains(o);
                }
            });
            LOGGER.debug("action=remove_excluded_suppliers status=finish number_of_excluded_suppliers={}",
                    demand.getExcludedSuppliers().size());
        }
    }

    /**
     * Removes all suppliers with admin acces roles.
     * @param suppliers
     */
    private void removeAdminSuppliers(Set<Supplier> suppliers) {
        LOGGER.debug("action=remove_admin_suppliers status=start");
        CollectionUtils.filter(suppliers, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ! AccessRole.isAdmin(((Supplier) o).getBusinessUser());
            }
        });
        LOGGER.debug("action=remove_excluded_suppliers status=finish");
    }

    private boolean sufficientRating(Integer supplierOveralRating, Demand demand) {
        return supplierOveralRating.compareTo(demand.getMinRating()) >= 0;
    }

}
