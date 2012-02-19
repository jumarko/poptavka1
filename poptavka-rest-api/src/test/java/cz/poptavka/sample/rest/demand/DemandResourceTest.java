/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.rest.common.dto.PageableCollectionDto;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.demand.DemandService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DemandResourceTest {

    private DemandResource demandResource;
    private List<Demand> demandsCollection = new ArrayList<Demand>();


    @Before
    public void setUp() {
        prepareDemandsCollection();

        final DemandService demandServiceMock = mock(DemandService.class);
        final GeneralService generalServiceMock = mock(GeneralService.class);
        when(generalServiceMock.search(any(Search.class)))
                .thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        final ISearch search = (ISearch) invocation.getArguments()[0];
                        if (!Demand.class.equals(search.getSearchClass())) {
                            fail("Search class has to be set to Demand.class, found: " + search.getSearchClass());
                        }

                        int fromIndex = search.getFirstResult();
                        if (fromIndex > (demandsCollection.size() - 1)) {
                            return Collections.EMPTY_LIST;
                        }
                        int toIndex = search.getFirstResult() + search.getMaxResults();
                        if (toIndex > demandsCollection.size()) {
                            toIndex = demandsCollection.size();
                        }
                        return demandsCollection.subList(fromIndex, toIndex);
                    }
                });

        final DemandResource demandResource = new DemandResource(demandServiceMock, new DemandSerializer(),
                new DemandDeserializer());
        demandResource.setGeneralService(generalServiceMock);
        this.demandResource = demandResource;
    }


    @Test
    public void testListPageSortById() {
        checkPage(this.demandResource.listPage("id", 0, 2), 2, "Demand1", "Demand2");
        checkPage(this.demandResource.listPage("id", 1, 2), 2, "Demand2", "Demand3");
        checkPage(this.demandResource.listPage("id", -1, 2), 2, "Demand1", "Demand2");
        checkPage(this.demandResource.listPage("id", 0, 9), 7,
                "Demand1", "Demand2", "Demand3", "Demand4", "Demand5", "Demand6", "Demand7");
        checkPage(this.demandResource.listPage("id", 8, 2), 0);

    }

    private void checkPage(PageableCollectionDto<cz.poptavka.sample.rest.demand.DemandDto> demandsPage,
            int expectedPageSize, String... expectedDemandTitles) {
        assertNotNull(demandsPage.getCollection());
        assertThat(demandsPage.getCollection().size(), is(expectedPageSize));
        final Iterator<cz.poptavka.sample.rest.demand.DemandDto> demandsIterator =
                demandsPage.getCollection().iterator();
        for (String expectedDemandTitle : expectedDemandTitles) {
            assertThat(demandsIterator.next().getTitle(), is(expectedDemandTitle));
        }
    }

    @Test
    public void testConvertToDtos() throws Exception {

    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private void prepareDemandsCollection() {

        this.demandsCollection.addAll(Arrays.asList(
                createDemand(1L),
                createDemand(2L),
                createDemand(3L),
                createDemand(4L),
                createDemand(5L),
                createDemand(6L),
                createDemand(7L)
        ));
    }

    private Demand createDemand(Long demandId) {
        final Demand demand = new Demand();
        demand.setId(demandId);
        demand.setTitle("Demand" + demand.getId());
        demand.setPrice(BigDecimal.valueOf(demandId * 1000));
        return demand;
    }


}
