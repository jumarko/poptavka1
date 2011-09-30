package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.user.Supplier;

/**
 * @author Juraj Martinka
 *         Date: 9/29/11
 *         Time: 9:31 PM
 */
public class PotentialSupplier {

    private static final int DEFAULT_RATING = 33;


    private Supplier supplier;

    /**
     * Number from 0 to 100 indicating the suitability of given supplier for given demand.
     * 0 is the lowest score, 100 is the highest score (the most suitable supplier).
     *
     * <STRONG>This is the rating of given supplier related to the result of search of suitable suppliers.
     * It has some connection with <code>Supplier#overalRating</code>
     * but these two attributes can be completely different. </STRONG>
     */
    private int rating;


    public PotentialSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.rating = DEFAULT_RATING;
    }

    public PotentialSupplier(Supplier supplier, int rating) {
        this.supplier = supplier;
        this.rating = rating;
    }


    public Supplier getSupplier() {
        return supplier;
    }

    public int getRating() {
        return rating;
    }
}
