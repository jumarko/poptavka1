package cz.poptavka.sample.application.scripting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class represents a script that can be executed by {@link ScriptExecutor}.
 *
 * @author Juraj Martinka
 *         Date: 24.1.11
 */
public class Script {


    /**
     * Enumeration of all usable script engines (script types) that can be used for evaluation of scripts.
     */
    public static enum SCRIPT_ENGINE {
        JAVA_SCRIPT("JavaScript");


        private final String value;

        SCRIPT_ENGINE(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "SCRIPT_ENGINE{"
                    + "value='" + value + '\''
                    + '}';
        }
    }


    private static final String NEW_LINE = "\n";
    private static final Logger LOGGER = LoggerFactory.getLogger(Script.class);

    private final String fileName;
    private final SCRIPT_ENGINE scriptEngine;

    /**
     *  Source code of this Script.
     * Once it is retrieved it is cached until this object is garbage collected.
     */
    private String scriptCode;


    /**
     * Creates new script which source code will be retrieved from <code>scriptFileName</code>.
     * File name is considered to be relative to current package (directory) of this class.
     * <p>IT IS PREFFERED to use 3-parametric constructor with non-empty source code.
     *
     * @param scriptFileName
     * @param scriptEngine
     *
     * @see #Script(String, cz.poptavka.sample.application.scripting.Script.SCRIPT_ENGINE, String)
     */
    public Script(String scriptFileName, SCRIPT_ENGINE scriptEngine) {
        this(scriptFileName, scriptEngine, null);
    }


    /**
     * Create new script.
     *
     * @param scriptFileName name of file which contains source code of this script - if <code>scriptSourceCode</code>
     * is not null than this file name is ignored
     * @param scriptEngine  available script engine which will be used for evaluation of <code>scriptSourceCode</code>.
     * @param scriptSourceCode
     */
    public Script(String scriptFileName, SCRIPT_ENGINE scriptEngine, String scriptSourceCode) {
        this.fileName = scriptFileName;
        this.scriptEngine = scriptEngine;
        this.scriptCode = scriptSourceCode;
    }


    public String getFileName() {
        return fileName;
    }

    public SCRIPT_ENGINE getScriptEngine() {
        return scriptEngine;
    }

    public String getScriptCode() {
        // double check idiom
        if (this.scriptCode == null) {
            try {
                this.scriptCode = getScriptCode(this);
            } catch (IOException e) {
                LOGGER.error("Source code of script " + scriptCode + " could not be retrieved.");
            }
        }

        return this.scriptCode;
    }


    @Override
    public String toString() {
        return "Script{"
                + "fileName='" + fileName + '\''
                + ", scriptEngine='" + scriptEngine + '\''
                + '}';
    }

    //----------------------------------- PRIVATE METHODS --------------------------------------------------------------
    private String getScriptCode(Script script) throws IOException {
        BufferedReader scriptReader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(script.getFileName())));

        StringBuilder scriptCode = new StringBuilder();
        String line = null;
        while ((line = scriptReader.readLine()) != null) {
            scriptCode.append(line).append(NEW_LINE);
        }

        return scriptCode.toString();
    }


}
