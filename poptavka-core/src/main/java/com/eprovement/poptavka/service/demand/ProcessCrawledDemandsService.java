package com.eprovement.poptavka.service.demand;

import com.eprovement.crawldemands.demand.Demand;

/**
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
public interface ProcessCrawledDemandsService {

    /**
     * Process all specified demands <code>newCrawledDemands</code>.
     * <p>
     *     These demands are crawled by external crawler and received by messaging.
     *     In usual situation they are simply converted to the appropriate domain objects and stored into the database.
     * @param newCrawledDemands
     */
    void processCrawledDemands(Demand[] newCrawledDemands);
}
