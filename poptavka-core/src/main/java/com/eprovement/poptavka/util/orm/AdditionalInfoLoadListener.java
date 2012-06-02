package com.eprovement.poptavka.util.orm;

import com.eprovement.poptavka.application.ApplicationContextHolder;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.AdditionalInfo;
import com.eprovement.poptavka.domain.common.AdditionalInfoAware;
import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.user.SupplierService;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;

/**
 *
 * @deprecated this listener cannot be used because it corrupts integration tests - demands count
 * and locality count must be stored physically in DB and refreshed on regular basis.
 *
 * Listener that ensures loadingo of additional info {@link com.eprovement.poptavka.domain.common.AdditionalInfo}
 * into the approriate entities.
 * Typical examples of additional info is demands count and suppliers count for locality or category.
 * <p>
 * Additional info is evaluated from other information and is not persistent. However, it cannot be presented
 * it entity class itself (e.g. {@link com.eprovement.poptavka.domain.address.Locality} or
 * {@link com.eprovement.poptavka.domain.demand.Category})
 * because GWT is not able to process this entities if they contains association to the service classes.
 *
 * @see com.eprovement.poptavka.domain.address.Locality
 * @see com.eprovement.poptavka.domain.demand.Category
 * @see com.eprovement.poptavka.domain.demand.DemandLocality
 * @see com.eprovement.poptavka.domain.demand.DemandCategory
 * @see com.eprovement.poptavka.service.demand.DemandService
 *
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
public class AdditionalInfoLoadListener implements PostLoadEventListener {

    private DemandService demandService;

    private SupplierService supplierService;

    private volatile boolean servicesInitialized = false;


    public AdditionalInfoLoadListener() {
    }


    /**
     * Load additional info for all entities that implements {@link AdditionalInfoAware}.
     *
     * <p>
     * At this moment only two entities with additional info exists:
     * {@link Locality} and {@link com.eprovement.poptavka.domain.demand.Category}
     * If more entities are being added in future than refactor this method to ensure reusabilitiy =>
     * NO SWITCH CLAUSE!
     *
     * @param event
     *
     * @see AdditionalInfo
     * @see AdditionalInfoAware
     */
    @Override
    public void onPostLoad(PostLoadEvent event) {
        ensureServicesInitialization();

        if (event.getEntity() instanceof AdditionalInfoAware) {
            final AdditionalInfoAware entityWithAddionalInfo = (AdditionalInfoAware) event.getEntity();

            Long demandsCount = null;
            Long suppliersCount = null;
            System.err.println("Entity id: " + ((DomainObject) entityWithAddionalInfo).getId());
            if (entityWithAddionalInfo instanceof Locality) {
                demandsCount = this.demandService.getDemandsCount(((Locality) entityWithAddionalInfo));
                suppliersCount = this.supplierService.getSuppliersCount(((Locality) entityWithAddionalInfo));
            } else if (entityWithAddionalInfo instanceof Category) {
                demandsCount = this.demandService.getDemandsCount(((Category) entityWithAddionalInfo));
                suppliersCount = this.supplierService.getSuppliersCount(((Category) entityWithAddionalInfo));
            }


            entityWithAddionalInfo.setAdditionalInfo(new AdditionalInfo(demandsCount, suppliersCount));
        }
    }


    //------------------------------ GETTTERS AND SETTERS --------------------------------------------------------------


    public DemandService getDemandService() {
        return demandService;
    }

    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    public SupplierService getSupplierService() {
        return supplierService;
    }

    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }


    //------------------------------------ HELPER METHODS --------------------------------------------------------------

    /**
     * Ensure that service depedencies are injected.
     * This must be done manually because JPA configuration ("entityManagerFactory") does not allowed to inject
     * dependencies into the listeners.
     */
    private void ensureServicesInitialization() {
        if (! servicesInitialized) {
            this.demandService =
                    (DemandService) ApplicationContextHolder.getApplicationContext().getBean("demandService");
            this.supplierService = (SupplierService) ApplicationContextHolder.getApplicationContext().
                    getBean("supplierService");
            this.servicesInitialized = true;
        }
    }
}
