package cz.poptavka.sample.domain.common;

/**
 * Inteface that should be implemented by all classes willing to know additonal information such as demands count
 * and suppliers count
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
