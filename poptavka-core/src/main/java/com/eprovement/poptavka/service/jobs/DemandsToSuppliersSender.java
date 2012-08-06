package com.eprovement.poptavka.service.jobs;

import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.jobs.base.JobTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

/**
 * TOTO create test of this class
 * @author Juraj Martinka
 *         Date: 10/4/11
 *         Time: 4:37 PM
 */
public class DemandsToSuppliersSender implements JobTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandsToSuppliersSender.class);

    private DemandService demandService;

    // TODO jumar separate logging into the aspect
    @Scheduled(cron = EVERY_MIDNIGHT)
    @Transactional
    public void execute() {
        LOGGER.info("DemandsToSuppliersSender status=start");
        this.demandService.sendDemandsToSuppliers();
        LOGGER.info("DemandsToSuppliersSender status=finish");
    }

    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }
}
