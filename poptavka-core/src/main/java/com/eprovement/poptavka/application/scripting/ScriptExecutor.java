package com.eprovement.poptavka.application.scripting;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

/**
 * Class that makes the execution of scripts simple.
 *
 * @author Juraj Martinka
 *         Date: 24.1.11
 * @see Script
 */
public final class ScriptExecutor {

    private static final ScriptExecutor INSTANCE = new ScriptExecutor();

    private final ScriptEngineManager mgr = new ScriptEngineManager();


    private ScriptExecutor() {
        System.out.println("all available script engines:");
        for (ScriptEngineFactory scriptEngineFactory : this.mgr.getEngineFactories()) {
            System.out.println("Name: " + scriptEngineFactory.getEngineName()
                    + ", version: " + scriptEngineFactory.getEngineVersion());
        }
    }

    public static ScriptExecutor getInstance() {
        return INSTANCE;
    }


    public void execute(Script script) throws ScriptException, IOException {
        if (script == null) {
            throw new IllegalArgumentException("Script to be executed cannot be null!");
        }
        ScriptEngine scriptEngine = mgr.getEngineByName(script.getScriptEngine().getValue());
        scriptEngine.eval(script.getScriptCode());
    }


    public void executeMethod(Script script, String function, Object... functionParams)  throws IOException,
            ScriptException, NoSuchMethodException {
        if (script == null) {
            throw new IllegalArgumentException("Script to be executed cannot be null!");
        }
        ScriptEngine scriptEngine = mgr.getEngineByName(script.getScriptEngine().getValue());
        scriptEngine.eval(script.getScriptCode());
        ((Invocable) scriptEngine).invokeFunction(function, functionParams);
    }


}
