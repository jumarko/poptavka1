package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.dao.user.SupplierDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.service.demand.SuppliersSelection;
import com.eprovement.poptavka.service.register.RegisterService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplierServiceImplTest {

    private SupplierServiceImpl supplierService;
    private DemandService demandServiceMock;
    private PotentialDemandService potentialDemandServiceMock;
    private SuppliersSelection suppliersSelectionMock;

    @Before
    public void setUp() {
        final SupplierServiceImpl supplierService = new SupplierServiceImpl(mock(GeneralService.class),
                mock(SupplierDao.class), mock(RegisterService.class), mock(UserVerificationService.class));

        this.demandServiceMock = mock(DemandService.class);
        supplierService.setDemandService(demandServiceMock);
        this.potentialDemandServiceMock = mock(PotentialDemandService.class);
        supplierService.setPotentialDemandService(potentialDemandServiceMock);
        this.suppliersSelectionMock = mock(SuppliersSelection.class);
        supplierService.setSuppliersSelection(suppliersSelectionMock);

        this.supplierService = supplierService;
    }

    // TODO: finish this test
    @Test
    public void sendPotentialDemandsToNewSupplier() {
        final Supplier myNewSupplier = new Supplier();
        final List<Category> category1 = Arrays.asList(createCategory(1L));
        final List<Locality> locality1 = Arrays.asList(createLocality(1L));
        myNewSupplier.setCategories(category1);
        myNewSupplier.setLocalities(locality1);
        when(demandServiceMock.getDemands(any(ResultCriteria.class), eq(category1), eq(locality1))).thenReturn(
                new HashSet(Arrays.asList(new Demand()))
        );

        supplierService.sendPotentialDemandsToNewSupplier(myNewSupplier);


    }

    private Category createCategory(long categoryId) {
        final Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    private Locality createLocality(long localityId) {
        final Locality locality = new Locality();
        locality.setId(localityId);
        return locality;
    }
}
