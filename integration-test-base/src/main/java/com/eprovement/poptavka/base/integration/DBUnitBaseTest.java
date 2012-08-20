package com.eprovement.poptavka.base.integration;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
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
 * which contains data to be loaded to the database. This path is specified through {@link @DataSet} annotation.
 * <p>
 * Even if no path has been specified, we try to load the file with name by convetion "UnqualifiedClassName.xml".
 * E.g. for class <code>ClientIntegrationTest</code> the path would be <code>ClientIntegrationTest.xml</code>.
 *
 * <p>
 * It is highly recommended to specify DTD file too - for our projects we use the common "test.dtd" file.
 * This file is generated from DB schema every time the integration tests are invoked.
 *
 * @see SpringDatabaseDataSourceConnection custom connection class used to NOT commit data into DB
 * @author Juraj Martinka
 *         Date: 9.1.11
 */
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

    /** Data type factory which is used by default.
     * See
     * <a href="http://theflashesofinsight.wordpress.com/2009/03/07/dbunitoracle-fix-issue-with-inserting-date-field/">
     *     Fix issue with inserting DATE Field</a>
     */
//    private static final IDataTypeFactory DATA_TYPE_FACTORY =  new org.dbunit.ext.hsqldb.HsqldbDataTypeFactory();
    private static final IDataTypeFactory DATA_TYPE_FACTORY =  new H2DataTypeFactory();

    // Commands for disabling/enabling foreign key checks - useful in some corner cases, e.g. circular
    // foreign key references
    private static final String DISABLE_FOREIGN_KEY_CHECKS_STATEMENT_HSQLDB =
            "SET DATABASE REFERENTIAL INTEGRITY FALSE";
    private static final String ENABLE_FOREIGN_KEY_CHECKS_STATEMENT_HSQLDB = "SET DATABASE REFERENTIAL INTEGRITY TRUE";
    private static final String DISABLE_FOREIGN_KEY_CHECKS_STATEMENT_H2 = "SET REFERENTIAL_INTEGRITY FALSE";
    private static final String ENABLE_FOREIGN_KEY_CHECKS_STATEMENT_H2 = "SET REFERENTIAL_INTEGRITY TRUE";
    private boolean foreignKeyChecksDisabled = false;

    @Autowired
    private DataSource dataSource;

    private volatile IDatabaseConnection connection;

    private List<String> dataSetsFilePaths = new ArrayList<String>();
    private String dataSetDtdFilePath  = "";


    @Before
    public void fillData() {
        generateDtdIfNeeded();

        try {
            disableForeignKeyChecksIfRequired();
            DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet());
        } catch (Exception rootCause) {
            throw new IllegalStateException("An exception occured while loading test data into the database"
                    + ", cause: " + rootCause.getMessage()
                    + ", DataSet: " + this.dataSetsFilePaths
                    + ", DTD: " + this.dataSetDtdFilePath
                    + ". Check the paths to the xml and DTD and verify that DTD is correctly bind to the xml.",
                    rootCause);
        }
    }

    /**
     * If client requires disabling of foreign key checks do this.
     * @throws Exception
     * @see @DataSet annotation
     */
    private synchronized void disableForeignKeyChecksIfRequired() throws Exception {
        if (this.getClass().getAnnotation(DataSet.class).disableForeignKeyChecks()) {
            // if disabling foreign key checks is required by annotation then do that
            getConnection().getConnection().prepareStatement(DISABLE_FOREIGN_KEY_CHECKS_STATEMENT_H2).execute();
            foreignKeyChecksDisabled = true;
        } else if (foreignKeyChecksDisabled) {
            // enable referential integrity checks again - if it has been disabled
            getConnection().getConnection().prepareStatement(ENABLE_FOREIGN_KEY_CHECKS_STATEMENT_H2).execute();
            foreignKeyChecksDisabled = false;
        }
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
        if (connection == null) {
            synchronized (this) {
                if (connection == null) {
                    final SpringDatabaseDataSourceConnection newConnection =
                            new SpringDatabaseDataSourceConnection(this.dataSource);
                    newConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                            DATA_TYPE_FACTORY);
                    this.connection = newConnection;
                }
            }
        }
        return  connection;
//        return new DatabaseDataSourceConnection(this.dataSource);
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

        // sequenceFilter for automatically ordering order tables by their foreign keys to avoid "FK constraint errors"
        // @see http://www.dbunit.org/faq.html#tableseq
        ITableFilter sequenceFilter = new DatabaseSequenceFilter(getConnection());

        final List<IDataSet> dataSets = new ArrayList<IDataSet>();
        for (String dataSetPath : this.dataSetsFilePaths) {
            final ReplacementDataSet dataSet = new ReplacementDataSet(dataSetBuilder.build(
                getResourceStream(dataSetPath)));
            dataSet.addReplacementObject(NULL_REPLACEMENT_VALUE, null);
            dataSets.add(new FilteredDataSet(sequenceFilter,  dataSet));
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
