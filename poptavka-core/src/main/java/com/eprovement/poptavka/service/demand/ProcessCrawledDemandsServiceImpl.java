package com.eprovement.poptavka.service.demand;

import com.google.common.base.Preconditions;
import com.eprovement.crawldemands.demand.Demand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
public class ProcessCrawledDemandsServiceImpl implements ProcessCrawledDemandsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCrawledDemandsServiceImpl.class);

    private Converter<Demand, com.eprovement.poptavka.domain.demand.Demand> demandConverter;
    private DemandService demandService;


    public ProcessCrawledDemandsServiceImpl(Converter demandConverter, DemandService demandService) {
        Preconditions.checkNotNull(demandConverter);
        Preconditions.checkNotNull(demandService);
        this.demandConverter = demandConverter;
        this.demandService = demandService;
    }


    public void processCrawledDemands(Demand[] newCrawledDemands) {
        try {
            for (Demand newCrawledDemand : newCrawledDemands) {
                final com.eprovement.poptavka.domain.demand.Demand newDomainDemand =
                        this.demandConverter.convert(newCrawledDemand);
                // store new demand
                this.demandService.create(newDomainDemand);
            }

        } catch (Exception e) {
            LOGGER.error("Exception while processing new incoming demand from crawler: " + e.getMessage());
            LOGGER.info("Try to continue with processing another demands.");
        }
    }


}
