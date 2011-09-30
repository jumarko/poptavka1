package cz.poptavka.sample.service.demand;


import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.PotentialSupplier;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.user.SupplierService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Juraj Martinka
 *         Date: 9/29/11
 *         Time: 9:34 PM
 */
public class NaiveSuppliersSelection implements SuppliersSelection {

    private SupplierService supplierService;

    @Override
    public Set<PotentialSupplier> getPotentialSuppliers(final Demand demand) {

        // find all possible suppliers
        final Set<Supplier> suppliers = new HashSet<Supplier>(100);
        suppliers.addAll(supplierService.getSuppliers(demand.getCategories().
                toArray(new Category[demand.getCategories().size()])));
        suppliers.addAll(supplierService.getSuppliers(demand.getLocalities().
                toArray(new Locality[demand.getLocalities().size()])));


        // TODO 1) client city can be used as some indicator
//        demand.getClient().getBusinessUser().getAddresses().get(0).getCity();

        // TODO 2) supplier's company name and description (see BusinessUserData) can be searched through
        // for occurrences of some words from demand's description
//        demand.getDescription();



        // exclude unwanted suppliers
        removeExcludedSuppliers(demand, suppliers);
        removeLowRatingSuppliers(demand, suppliers);


        // convert to set of PotentialSupplier-s  sorted by rating
        final SortedSet<PotentialSupplier> potentialSupplierSet = convertToPotentialSuppliersSet(suppliers);

        // if neccessary - leave out the suppliers with lowest rating
        removeSuppliersBeyondMax(demand, potentialSupplierSet);

        return potentialSupplierSet;
    }

    /**
     * Check if set <code>potentialSupplierSet</code> contains more than <code>demand#getMaxSuppliers</code> and if so,
     * then remove as many "lowest" (with lowest rating) suppliers as neccessary to satisfy limit.
     * @param demand
     * @param potentialSupplierSet
     */
    private void removeSuppliersBeyondMax(Demand demand, SortedSet<PotentialSupplier> potentialSupplierSet) {
        if (demand.getMaxSuppliers() != null
                && (potentialSupplierSet.size() > demand.getMaxSuppliers())) {

            // number of elements that have to be removed
            final int overflowElementsCount = potentialSupplierSet.size() - demand.getMaxSuppliers();

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

                        if (potentialSupplierOne.getSupplier().getOveralRating()
                                .compareTo(potentialSupplierTwo.getSupplier().getOveralRating()) != 0) {

                            return potentialSupplierOne.getSupplier().getOveralRating()
                                    .compareTo(potentialSupplierTwo.getSupplier().getOveralRating());
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
            CollectionUtils.filter(suppliers, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    final Integer supplierOveralRating = ((Supplier) o).getOveralRating();
                    return supplierOveralRating == null
                            || sufficientRating(supplierOveralRating, demand);

                }
            });
        }
    }

    private void removeExcludedSuppliers(final Demand demand, Set<Supplier> suppliers) {
        if (CollectionUtils.isNotEmpty(demand.getExcludedSuppliers())) {
            CollectionUtils.filter(suppliers, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return demand.getExcludedSuppliers().contains(o);
                }
            });
        }
    }

    private boolean sufficientRating(Integer supplierOveralRating, Demand demand) {
        return supplierOveralRating.compareTo(demand.getMinRating()) >= 0;
    }

    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
}
