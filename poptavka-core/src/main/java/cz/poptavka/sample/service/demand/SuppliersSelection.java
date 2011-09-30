package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.PotentialSupplier;

import java.util.Set;

/**
 * Encapsulates algorithm for selection of suitable suppliers for some demand.
 * @author Juraj Martinka
 *         Date: 9/29/11
 *         Time: 9:29 PM
 */
public interface SuppliersSelection {

    Set<PotentialSupplier> getPotentialSuppliers(Demand demand);
}

