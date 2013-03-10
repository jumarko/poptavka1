package com.eprovement.poptavka.service.jobs;

import com.eprovement.poptavka.domain.common.AdditionalInfo;
import com.eprovement.poptavka.domain.common.AdditionalInfoAware;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.jobs.base.Job;
import com.eprovement.poptavka.service.user.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * A job which is used for regular filling of additional info for all entities that implements
 * interface {@link com.eprovement.poptavka.domain.common.AdditionalInfoAware}.
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
public class AdditionalInfoFiller implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalInfoFiller.class);

    private static final long NO_DEMANDS = 0L;
    private static final Long NO_SUPPLIERS = 0L;

    private DemandService demandService;
    private SupplierService supplierService;


    @Scheduled(cron = EVERY_MIDNIGHT)
    @Transactional
    public void execute() {
        LOGGER.info("Localities additional info filling status=start");
        final long localitiesFillStartTime = System.currentTimeMillis();
        setAdditionalInfoForAllItems(this.demandService.getDemandsCountForAllLocalities(),
                this.supplierService.getSuppliersCountForAllLocalities());
        LOGGER.info("Localities additional info filling status=finish duration=["
                + (System.currentTimeMillis() - localitiesFillStartTime) + "] ms.");

        // fill categories' info
        LOGGER.info("Categories additional info filling status=start");
        final long categoriesFillStartTime = System.currentTimeMillis();
        setAdditionalInfoForAllItems(this.demandService.getDemandsCountForAllCategories(),
                this.supplierService.getSuppliersCountForAllCategories());
        LOGGER.info("Categories additional info filling status=finish duration=["
                + (System.currentTimeMillis() - categoriesFillStartTime) + "] ms.");
    }

    @Override
    public String description() {
        return "AdditionaInfoFiller is useful for periodical refresh of table AdditionalInfo which contains"
                + " demands count and suppliers count for localities and categories."
                + " The freshness of result which end-user see on poptavka UI depends on frequency"
                + " with which this job is executed.";
    }


    //------------------------------- PRIVATE METHODS ------------------------------------------------------------------
    private void setAdditionalInfoForAllItems(Map<? extends AdditionalInfoAware, Long> demandsCountForAllItems,
                                              Map<? extends AdditionalInfoAware, Long> suppliersCountForAllItems) {

        try {
            for (Map.Entry<? extends AdditionalInfoAware, Long> demandsCountEntry
                    : demandsCountForAllItems.entrySet()) {
                final AdditionalInfoAware additionalInfoAware = demandsCountEntry.getKey();
                setAdditionalInfo(additionalInfoAware, demandsCountEntry.getValue(),
                        suppliersCountForAllItems.get(additionalInfoAware));
            }
        } catch (Exception e) {
            LOGGER.error("additional info filling status=error", e);
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
