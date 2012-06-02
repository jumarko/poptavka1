package com.eprovement.poptavka.application.logging;

import com.eprovement.poptavka.base.integration.BasicIntegrationTest;
import com.eprovement.poptavka.dao.demand.DemandDao;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.service.GenericService;
import com.eprovement.poptavka.service.demand.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 10.4.11
 */
public class ExceptionLoggerTest extends BasicIntegrationTest {

    @Autowired
    private ExceptionLogger exceptionLogger;
    // spy for verification of interactions on real bean
    private ExceptionLogger exceptionLoggerSpy;

    @Autowired
    private ExecutionTimeLogger executionTimeLogger;
    // spy for verification of interactions on real bean
    private ExecutionTimeLogger executionTimeLoggerSpy;


    private CategoryService categoryService;

    private DemandDao demandDao;

    private GenericService genericService;

    private Locality locality;

    private Address address;


    @Before
    public void setUp() throws Exception {

//        Assert.assertNotNull(this.exceptionLogger);
//        this.exceptionLoggerSpy = Mockito.spy(this.exceptionLogger);
//
//        Assert.assertNotNull(this.executionTimeLogger);
//        this.executionTimeLoggerSpy = Mockito.spy(this.executionTimeLogger);
//
//        this.categoryService = new CategoryServiceImpl();
////        this.categoryService = Mockito.mock(CategoryService.class);
//        this.demandDao = new DemandDaoImpl();
//        this.genericService = new CategoryServiceImpl();
//        this.locality = Mockito.mock(Locality.class);
//        this.address= Mockito.mock(Address.class);
    }

    @Test
    public void testLogExceptionMethod() throws Exception {
//        final RuntimeException exceptionInAdvisedMethod =
//                new RuntimeException("Great exception to be caught by exceptionLoggerAspect!");
//        Mockito.when(categoryService.getCategory("cat1")).thenThrow(exceptionInAdvisedMethod);

//        Mockito.verify(exceptionLoggerSpy).logExceptionMethod(exceptionInAdvisedMethod);

//        this.address.getCity();
//        Mockito.verify(executionTimeLoggerSpy).logTimeMethod(Mockito.<ProceedingJoinPoint>any());

//        new Address().getCity().getChildren();
    }
}
