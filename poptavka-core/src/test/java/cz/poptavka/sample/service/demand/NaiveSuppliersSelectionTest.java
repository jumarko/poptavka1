package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.PotentialSupplier;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.user.SupplierService;
import junit.framework.TestCase;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

        final Set<Supplier> localitySuppliers = new HashSet<Supplier>();
        localitySuppliers.add(createSupplier(1L, 10));
        localitySuppliers.add(createSupplier(2L, 1));
        localitySuppliers.add(createSupplier(3L, 3));
        localitySuppliers.add(createSupplier(4L, 29));
        localitySuppliers.add(createSupplier(5L, 90));
        localitySuppliers.add(createSupplier(6L, 13));
        localitySuppliers.add(createSupplier(7L, 21));
        localitySuppliers.add(createSupplier(8L, 18));
        localitySuppliers.add(createSupplier(9L, 30));
        Mockito.when(supplierServiceMock.getSuppliers(Mockito.<Locality>anyVararg()))
                .thenReturn(localitySuppliers);

        final Set<Supplier> categorySuppliers = new HashSet<Supplier>();
        categorySuppliers.add(createSupplier(11L, 100));
        categorySuppliers.add(createSupplier(12L, 10));
        categorySuppliers.add(createSupplier(13L, 30));
        categorySuppliers.add(createSupplier(14L, 29));
        categorySuppliers.add(createSupplier(15L, 88));
        categorySuppliers.add(createSupplier(16L, 13));
        Mockito.when(supplierServiceMock.getSuppliers(Mockito.<Category>anyVararg()))
                .thenReturn(categorySuppliers);


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
        org.junit.Assert.assertThat(demandPotentialSuppliers.size(), Is.is(5));
    }
}
