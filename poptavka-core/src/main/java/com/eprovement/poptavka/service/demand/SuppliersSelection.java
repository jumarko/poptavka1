package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;

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

