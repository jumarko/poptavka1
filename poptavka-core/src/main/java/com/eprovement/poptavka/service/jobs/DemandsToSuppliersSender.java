package com.eprovement.poptavka.service.jobs;

import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.service.jobs.base.JobTask;
import org.apache.commons.lang.Validate;
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

    private final PotentialDemandService potentialDemandService;

    public DemandsToSuppliersSender(PotentialDemandService potentialDemandService) {
        Validate.notNull(potentialDemandService);
        this.potentialDemandService = potentialDemandService;
    }

    // TODO jumar separate logging into the aspect
    @Scheduled(cron = EVERY_MIDNIGHT)
    @Transactional
    public void execute() {
        LOGGER.info("DemandsToSuppliersSender status=start");
        this.potentialDemandService.sendDemandsToPotentialSuppliers();
        LOGGER.info("DemandsToSuppliersSender status=finish");
    }

}
