/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;


/**
 *
 * @author Martin Slavkovsky
 */
public class LocalitySuggestionDetail //MultiWordSuggestOracle.MultiWordSuggestion
        implements IsSerializable, Suggestion {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Suggestion. **/
    private String displayString;
    private String replacementString;
    /** State. **/
    private long stateId;
    private String stateName;
    /** City. **/
    private long cityId;
    private String cityName;

    /**************************************************************************/
    /* INITIALIZATOIN                                                         */
    /**************************************************************************/
    public LocalitySuggestionDetail() {
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /** Suggestion. **/
    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public void setReplacementString(String replacementString) {
        this.replacementString = replacementString;
    }

    public void setSuggestion(String replacementString, String displayString) {
        this.replacementString = replacementString;
        this.displayString = displayString;
    }

    /** State. **/
    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /** City. **/
    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** Suggestion. **/
    @Override
    public String getDisplayString() {
        return displayString;
    }

    @Override
    public String getReplacementString() {
        return replacementString;
    }

    /** State. **/
    public long getStateId() {
        return stateId;
    }

    public String getStateName() {
        return stateName;
    }

    /** City. **/
    public long getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return getCityName() + ", " + getStateName();
    }
}
