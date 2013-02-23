package com.eprovement.poptavka.domain.common;

/**
 * Interface that should be implemented by all classes willing to know additional information such as demands count
 * and suppliers count.
 *
 * @see com.eprovement.poptavka.util.orm.AdditionalInfoLoadListener
 * @see com.eprovement.poptavka.domain.address.Locality
 * @see com.eprovement.poptavka.domain.demand.Category
 *
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
public interface AdditionalInfoAware {

    AdditionalInfo getAdditionalInfo();

    void setAdditionalInfo(AdditionalInfo additionalInfo);
}
