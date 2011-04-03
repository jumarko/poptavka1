package cz.poptavka.sample.service.jobs;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.AdditionalInfo;
import cz.poptavka.sample.domain.common.AdditionalInfoAware;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * A job which is used for regular filling of additional info for all entities that implements
 * interface {@link cz.poptavka.sample.domain.common.AdditionalInfoAware}.
 * <p>
 * Additional info is computed and then inserted into the DB.
 * <p>
 * The default interval is 1 hour.
 *
 *
 *
 * @author Juraj Martinka
 *         Date: 3.4.11
 */
public class AdditionalInfoFiller {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalInfoFiller.class);

    private static final int ONE_HOUR = 3600000;

    private DemandService demandService;
    private SupplierService supplierService;

    private LocalityService localityService;
    private CategoryService categoryService;


    @Scheduled(fixedRate = ONE_HOUR)
    public void execute() {
        LOGGER.info("Job AdditionalInfoFiller is being executed...");

        // fill localities' info
        final List<Locality> allLocalities = this.localityService.getAll();
        for (Locality locality : allLocalities) {
            initAdditionalInfo(locality);

            locality.getAdditionalInfo().setDemandsCount(this.demandService.getDemandsCount(locality));
            locality.getAdditionalInfo().setSuppliersCount(this.supplierService.getSuppliersCount(locality));
        }

        // fill categories' info
        final List<Category> allCategories = this.categoryService.getAll();
        for (Category category : allCategories) {
            initAdditionalInfo(category);

            category.getAdditionalInfo().setDemandsCount(this.demandService.getDemandsCount(category));
            category.getAdditionalInfo().setSuppliersCount(this.supplierService.getSuppliersCount(category));
        }
    }


    private void initAdditionalInfo(AdditionalInfoAware additionalInfoAware) {
        if (additionalInfoAware.getAdditionalInfo() == null) {
            additionalInfoAware.setAdditionalInfo(new AdditionalInfo(0L, 0L));
        }
    }


    //------------------- GETTERS AND SETTERS --------------------------------------------------------------------------

    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
