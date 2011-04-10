package cz.poptavka.sample.base.integration;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.application.ApplicationContextHolder;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Generator for creating dtd for DBUnit integrations tests' scripts (XML files for inserting temporary testing data).
 * <p>
 * It provides functionality for testing and it must be placed in "test" sources directory.
 * <p>
 * The regeneration of "test.dtd" is needed if some changes, that might have an impact on integration testing,
 *  in domain model classes will occur,
            // .
 *
 * @author Juraj Martinka
 *         Date: 30.1.11
 */
public final class DbUnitDtdGenerator {

    /** This must be correct path into the dtd file withing poptavka-core module! */
    private static final String GENERATED_DTD_PATH = "test.dtd";

    private static final Logger LOGGER = LoggerFactory.getLogger(DbUnitDtdGenerator.class);


    private DbUnitDtdGenerator() {
        // DO NOT INSTANTIATE utility class
    }

    /**
     * Generate dtd for integration tests from schema retrieved from given <code>dataSource</code>.
     *
     * @param dataSource dataSource which will be used for generation of dtd file.
     * If null then method tries to get default data source
     */
    public static void generateDtd(DataSource dataSource) {
        if (dataSource == null) {
            dataSource = (DataSource) ApplicationContextHolder.getApplicationContext().getBean("poptavkaDataSource");
        }
        Preconditions.checkState(dataSource != null, "Data source must be defined for dtd generation.");

        try {
            // quite ugly to get resource and write to it, but it is guaranteed that this resource is a file in project
            final URL dtdUrl = DbUnitDtdGenerator.class.getClassLoader().getResource(GENERATED_DTD_PATH);
            FlatDtdDataSet.write(new DatabaseDataSourceConnection(dataSource).createDataSet(),
                    new FileOutputStream(new File(dtdUrl.toURI())));
            LOGGER.info("Dtd file [" + GENERATED_DTD_PATH + "] for integration tests has been generated.");
        } catch (Exception e) {
            throw new DtdGenerationException("An exception occured while generating dtd from databse "
                    + ", cause: " + e.getMessage());
        }
    }


    public static class DtdGenerationException extends RuntimeException {
        public DtdGenerationException(String message) {
            super(message);
        }
    }
}
