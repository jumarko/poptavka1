package com.eprovement.poptavka.service.system;

import com.eprovement.poptavka.domain.settings.SystemProperties;
import com.eprovement.poptavka.service.GeneralService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.Validate;

/**
 * @author Martin Slavkovsky
 */
public final class SystemPropertiesService {

    private static final String IMMEDIATE_DEMAND_COUNTS = "immediateIncrementalDemandCounts";
    private static final String IMMEDIATE_SUPPLIER_COUNTS = "immediateIncrementalSupplierCounts";

    private GeneralService generalService;

    public SystemPropertiesService(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**
     * @return get <code>{@value SystemProperties#IMEDIATE_DEMANDS_COUNTS}</code> property
     */
    public boolean isImediateDemandCount() {
        return Boolean.parseBoolean(getProperties(IMMEDIATE_DEMAND_COUNTS).getValue());
    }

    /**
     * @return get <code>{@value SystemProperties#IMMEDIATE_SUPPLIER_COUNTS}</code> property
     */
    public boolean isImediateSupplierCount() {
        return Boolean.parseBoolean(getProperties(IMMEDIATE_SUPPLIER_COUNTS).getValue());
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
