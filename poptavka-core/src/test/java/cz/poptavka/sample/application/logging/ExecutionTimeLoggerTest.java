package cz.poptavka.sample.application.logging;

import cz.poptavka.sample.base.integration.BasicIntegrationTest;
import cz.poptavka.sample.service.jobs.base.JobTask;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Juraj Martinka
 *         Date: 10/4/11
 *         Time: 5:25 PM
 */
public class ExecutionTimeLoggerTest extends BasicIntegrationTest {

    @Autowired @Qualifier("demandsToSuppliersSender")
    private JobTask demandsToSuppliersSender;

    @Autowired @Qualifier("additionalInfoFiller")
    private JobTask additionalInfoFiller;

    @Autowired
    private ExecutionTimeLogger executionTimeLogger;


    @Ignore
    @Test
    public void testLogTimeMethod() throws Exception {
        final ExecutionTimeLogger executionTimeLoggerSpy = Mockito.spy(this.executionTimeLogger);
        this.demandsToSuppliersSender.execute();
        this.additionalInfoFiller.execute();

        // TODO check is done manually in log file - replace with automatic verficiation
        try {
            Mockito.verify(executionTimeLoggerSpy, new Times(1)).logTimeMethod(Mockito.any(ProceedingJoinPoint.class));
        } catch (Throwable throwable) {
            Assert.fail(throwable.getMessage());
        }

    }
}
