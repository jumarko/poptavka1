package com.eprovement.poptavka.service.system;

import com.eprovement.poptavka.domain.system.SystemProperties;
import com.eprovement.poptavka.service.GeneralService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.Validate;

/**
 * @author Martin Slavkovsky
 */
public class SystemPropertiesServiceImpl implements SystemPropertiesService {

    private GeneralService generalService;

    public SystemPropertiesServiceImpl(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isImediateDemandCount() {
        return Boolean.parseBoolean(getProperties(SystemPropertiesService.IMMEDIATE_DEMAND_COUNTS).getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isImediateSupplierCount() {
        return Boolean.parseBoolean(getProperties(SystemPropertiesService.IMMEDIATE_SUPPLIER_COUNTS).getValue());
    }

    /**
     * Retireve property from db by given key.
     * If no property found, expection is throwed.
     *
     * @param key unique indentifier for property
     * @return found property
     */
    private SystemProperties getProperties(String key) {
        Search search = new Search(SystemProperties.class)
            .addFilterEqual("key", key);
        SystemProperties properties = (SystemProperties) generalService.searchUnique(search);
        Validate.notNull(properties, "Property '" + key + "' is not set");
        return properties;
    }
}
