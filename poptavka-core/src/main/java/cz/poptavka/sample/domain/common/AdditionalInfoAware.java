package cz.poptavka.sample.domain.common;

/**
 * Interface that should be implemented by all classes willing to know additional information such as demands count
 * and suppliers count.
 *
 * @see cz.poptavka.sample.util.orm.AdditionalInfoLoadListener
 * @see cz.poptavka.sample.domain.address.Locality
 * @see cz.poptavka.sample.domain.demand.Category
 *
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
public interface AdditionalInfoAware {

    AdditionalInfo getAdditionalInfo();

    void setAdditionalInfo(AdditionalInfo additionalInfo);
}
