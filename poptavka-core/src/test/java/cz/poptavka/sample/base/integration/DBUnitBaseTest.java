package cz.poptavka.sample.base.integration;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Base class for all integration tests in project.
 * <p>
 * It prepares data for tests, the only one thing the client (subclass) must specify is the path to the xml file
 * which contains data to be loaded to the database. This path is specified through {@liuk @DataSet} annotation.
 * <p>
 * Even if no path has been specified, we try to load the file with name by convetion "UnqualifiedClassName.xml".
 * E.g. for class <code>ClientIntegrationTest</code> the path would be <code>ClientIntegrationTest.xml</code>.
 *
 * <p>
 * It is very suitable to specify also DTD file - for our projects we use the common "test.dtd" file.
 * This file is generated from DB schema every time the integration tests are invoked.
 *
 * @author Juraj Martinka
 *         Date: 9.1.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
// must be transactional to roll-back the created data after each test
@Transactional
public abstract class DBUnitBaseTest {

    /** Default suffix which will be used when constructing default name of xml file. **/
    private static final String DEFAULT_SUFFIX = "DataSet.xml";
    /** Default package (directory) which contains xml scripts for loading data. */
    private static final String DEFAULT_DATA_PACKAGE = "data";
    private static final String PACKAGE_SEPARATOR = "/";
    private static final String DTD_SUFFIX = "DataSet.dtd";
    private static final String CLASSPATH = "classpath:";

    /** Indicates whether if the "test.dtd" has been regenerated within this round of integration tests run. */
    private static volatile boolean dtdGenerated = false;
    /** String that can be used in xml scripts to represent null values. */
    private static final String NULL_REPLACEMENT_VALUE = "[NULL]";

    @Autowired
    private DataSource dataSource;

    private List<String> dataSetsFilePaths = new ArrayList<String>();
    private String dataSetDtdFilePath  = "";


    @Before
    public void fillData() {
        generateDtdIfNeeded();

        try {
            DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet());
        } catch (Exception e) {
            throw new IllegalStateException("An exception occured while loading test data into the database"
                    + ", cause: " + e.getMessage()
                    + ", DataSet: " + this.dataSetsFilePaths
                    + ", DTD: " + this.dataSetDtdFilePath
                    + ". Check the paths to the xml and DTD and verify that DTD is correctly bind to the xml.");
        }
    }


    @After
    public void cleanData() {
        // TODO martinka: this does not works as expected - probably because of data definition order
        // e.g. in BaseDataSet.xml
//        try {
//            DatabaseOperation.DELETE.execute(getConnection(), getDataSet());
//        } catch (Exception e) {
//            throw new IllegalStateException("An exception occured while cleaning test data from the database"
//                    + ", cause: " + e.getMessage()
//                    + ", DataSet: " + this.dataSetsFilePaths
//                    + ", DTD: " + this.dataSetDtdFilePath
//                    + ". Check the paths to the xml and DTD and verify that DTD is correctly bind to the xml.");
//        }
    }


    /**
     * Get relative path to the DataSet xml file for loading the database.
     * This file can be placed only directly in package where the running test is located or in its subpackages.
     * This enforce the practical rule about location and should not be changed.
     * @return
     */
    protected List<String> getDataSetPath() {
        DataSet dataSet = this.getClass().getAnnotation(DataSet.class);
        List<String> paths = dataSet != null ? Arrays.asList(dataSet.path()) : new ArrayList<String>();
        if (paths.isEmpty()) {
            // user does not specified meaningful path for xml script, try to find it by naming convetion
            paths.add(DEFAULT_DATA_PACKAGE + PACKAGE_SEPARATOR + this.getClass().getSimpleName() + DEFAULT_SUFFIX);
        }

        return  paths;
    }

    /**
     * Get the location of DTD file which is used for validation of xml DataSet file.
     * This DTD file could be placed arbitrarily on classpath, because it is useful to share this file among multiple
     * xml DataSet files.
     *
     * @param xmlFilePath
     * @return
     */
    protected String getDataSetDtdPath(String xmlFilePath) {
        Assert.isTrue(StringUtils.isNotBlank(xmlFilePath));

        final DataSet dataSet = this.getClass().getAnnotation(DataSet.class);
        String dtdPath = dataSet != null ? dataSet.dtd() : "";

        if (StringUtils.isBlank(dtdPath)) {
            dtdPath = xmlFilePath.substring(0,
                xmlFilePath.length() - DEFAULT_SUFFIX.length())
                + DTD_SUFFIX;
        }

        return dtdPath;
    }


    //--------------------------------- HELPER METHODS -----------------------------------------------------------------
    private synchronized void generateDtdIfNeeded() {
        if (! dtdGenerated) {
            DbUnitDtdGenerator.generateDtd(this.dataSource);
            dtdGenerated = true;
        }
    }


    private IDatabaseConnection getConnection() throws Exception {
        return new DatabaseDataSourceConnection(this.dataSource);
    }


    private IDataSet getDataSet() throws Exception {

        this.dataSetsFilePaths = getDataSetPath();
        Preconditions.checkState(! this.dataSetsFilePaths.isEmpty());

        this.dataSetDtdFilePath = getDataSetDtdPath(this.dataSetsFilePaths.get(0));

        final FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();

        // force the optional columns don't have to be added for all entities in xml data set file
        dataSetBuilder.setColumnSensing(true);

        InputStream dtdResourceStream = getResourceStream(this.dataSetDtdFilePath);
        if (dtdResourceStream != null) {
            dataSetBuilder.setMetaDataSetFromDtd(dtdResourceStream);
        }

        final List<IDataSet> dataSets = new ArrayList<IDataSet>();
        for (String dataSetPath : this.dataSetsFilePaths) {
            final ReplacementDataSet dataSet = new ReplacementDataSet(dataSetBuilder.build(
                getResourceStream(dataSetPath)));
            dataSet.addReplacementObject(NULL_REPLACEMENT_VALUE, null);
            dataSets.add(dataSet);
        }

        return new CompositeDataSet(dataSets.toArray(new IDataSet[dataSets.size()]));
    }


    private InputStream getResourceStream(String resourceName) {
        InputStream resourceStream = null;
        if (resourceName.contains(CLASSPATH)) {
            // remove prefix "classpath:"
            resourceName = resourceName.substring(
                    CLASSPATH.length(),
                    resourceName.length())
                    .trim();
            resourceStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        } else {
            resourceStream = getClass().getResourceAsStream(resourceName);
        }
        return resourceStream;
    }

}
