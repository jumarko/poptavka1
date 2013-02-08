package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.user.SupplierService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import org.mockito.Mockito;

/**
 * @author Juraj Martinka
 *         Date: 9/29/11
 *         Time: 10:45 PM
 */
public class NaiveSuppliersSelectionTest extends TestCase {

    private SuppliersSelection naiveSuppliersSelection = new NaiveSuppliersSelection();

    @Before
    public void setUp() {
        final SupplierService supplierServiceMock = Mockito.mock(SupplierService.class);

        final Set<Supplier> suppliers = new HashSet<Supplier>();
        suppliers.add(createSupplier(1L, 10));
        suppliers.add(createSupplier(2L, 1));
        suppliers.add(createSupplier(3L, 3));
        suppliers.add(createSupplier(4L, 29));
        suppliers.add(createSupplier(5L, 90));
        suppliers.add(createSupplier(6L, 13));
        suppliers.add(createSupplier(7L, 21));
        suppliers.add(createSupplier(8L, 18));
        suppliers.add(createSupplier(9L, 30));
        Mockito.when(supplierServiceMock.getSuppliers(any(ResultCriteria.class), anyListOf(Category.class),
                anyListOf(Locality.class)))
                .thenReturn(suppliers);


        ((NaiveSuppliersSelection) this.naiveSuppliersSelection).setSupplierService(supplierServiceMock);


    }

    private Supplier createSupplier(Long id, int overallRating) {
        final Supplier supplier1 = new Supplier();
        supplier1.setId(id);
        supplier1.setOveralRating(overallRating);
        return supplier1;
    }


    @Test
    public void testGetPotentialSuppliers() {

        final Demand demand = new Demand();
        demand.setCategories(Arrays.asList(new Category()));
        demand.setLocalities(Arrays.asList(new Locality()));
        demand.setMinRating(25);
        demand.setMaxSuppliers(5);
        demand.setStatus(DemandStatus.NEW);
        demand.setClient(new Client());
        final Set<PotentialSupplier> demandPotentialSuppliers =
                this.naiveSuppliersSelection.getPotentialSuppliers(demand);
        org.junit.Assert.assertThat(demandPotentialSuppliers.size(), Is.is(3));
    }
}
