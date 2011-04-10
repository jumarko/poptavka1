package cz.poptavka.sample.service.jobs;

import cz.poptavka.sample.domain.common.AdditionalInfo;
import cz.poptavka.sample.domain.common.AdditionalInfoAware;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.jobs.base.JobTask;
import cz.poptavka.sample.service.user.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * A job which is used for regular filling of additional info for all entities that implements
 * interface {@link cz.poptavka.sample.domain.common.AdditionalInfoAware}.
 * <p>
 * Additional info is computed and then inserted into the DB.
 * <p>
 * The default interval is 2 hour.
 *
 * <p>
 * This implementation can take aproximately 20 minutes to accomplish the job on a developer machine
 * with remote access to the database.
 *
 * @author Juraj Martinka
 *         Date: 3.4.11
 */
public class AdditionalInfoFiller implements JobTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalInfoFiller.class);

    private static final long NO_DEMANDS = 0L;
    private static final Long NO_SUPPLIERS = 0L;

    private DemandService demandService;
    private SupplierService supplierService;



    @Scheduled(cron = EVERY_MIDNIGHT)
    @Transactional
    public void execute() {
        LOGGER.info("Job AdditionalInfoFiller is being executed...");

//        // fill localities' info
        setAdditionalInfoForAllItems(this.demandService.getDemandsCountForAllLocalities(),
                this.supplierService.getSuppliersCountForAllLocalities());

        // fill categories' info
        setAdditionalInfoForAllItems(this.demandService.getDemandsCountForAllCategories(),
                this.supplierService.getSuppliersCountForAllCategories());

        LOGGER.info("Job AdditionalInfoFiller has finished.");
    }


    //------------------------------- PRIVATE METHODS ------------------------------------------------------------------
    private void setAdditionalInfoForAllItems(Map<? extends AdditionalInfoAware, Long> demandsCountForAllItems,
                                              Map<? extends AdditionalInfoAware, Long> suppliersCountForAllItems) {

        for (Map.Entry<? extends AdditionalInfoAware, Long> demandsCountEntry : demandsCountForAllItems.entrySet()) {
            final AdditionalInfoAware additionalInfoAware = demandsCountEntry.getKey();
            setAdditionalInfo(additionalInfoAware, demandsCountEntry.getValue(),
                    suppliersCountForAllItems.get(additionalInfoAware));
        }
    }


    private void setAdditionalInfo(AdditionalInfoAware additionalInfoAware, Long demandsCount, Long suppliersCount) {
        initAdditionalInfo(additionalInfoAware);
        if (demandsCount != null) {
            additionalInfoAware.getAdditionalInfo().setDemandsCount(demandsCount);
        }
        if (suppliersCount != null) {
            additionalInfoAware.getAdditionalInfo().setSuppliersCount(suppliersCount);
        }
    }


    private void initAdditionalInfo(AdditionalInfoAware additionalInfoAware) {
        if (additionalInfoAware.getAdditionalInfo() == null) {
            additionalInfoAware.setAdditionalInfo(new AdditionalInfo(NO_DEMANDS, NO_SUPPLIERS));
        }
    }


    //------------------- GETTERS AND SETTERS --------------------------------------------------------------------------

    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
}
