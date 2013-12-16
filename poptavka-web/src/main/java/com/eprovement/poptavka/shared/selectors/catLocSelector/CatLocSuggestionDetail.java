/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.selectors.catLocSelector;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 *
 * @author Martin Slavkovsky
 */
public class CatLocSuggestionDetail //MultiWordSuggestOracle.MultiWordSuggestion
        implements IsSerializable, Suggestion {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Suggestion. **/
    private String displayString;
    private String replacementString;
    /** Categories. **/
    private ICatLocDetail parentCatLoc;
    private ICatLocDetail catLoc;

    /**************************************************************************/
    /* INITIALIZATOIN                                                         */
    /**************************************************************************/
    public CatLocSuggestionDetail() {
        //for serialization
    }

    public CatLocSuggestionDetail(ICatLocDetail parentCatLoc, ICatLocDetail catLoc) {
        this.parentCatLoc = parentCatLoc;
        this.catLoc = catLoc;
    }

    /**************************************************************************/
    /* GETTERS & SETTERS                                                      */
    /**************************************************************************/
    /** Suggestion. **/
    @Override
    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    @Override
    public String getReplacementString() {
        return replacementString;
    }

    public void setReplacementString(String replacementString) {
        this.replacementString = replacementString;
    }

    public void setSuggestion(String replacementString, String displayString) {
        this.replacementString = replacementString;
        this.displayString = displayString;
    }

    public ICatLocDetail getParentCatLoc() {
        return parentCatLoc;
    }

    public void setParentCatLoc(ICatLocDetail parentCatLoc) {
        this.parentCatLoc = parentCatLoc;
    }

    public ICatLocDetail getCatLoc() {
        return catLoc;
    }

    public void setCatLoc(ICatLocDetail selectedCategory) {
        this.catLoc = selectedCategory;
    }

    /**************************************************************************/
    /* Override methods                                                       */
    /**************************************************************************/
    @Override
    public int hashCode() {
        return (int) catLoc.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatLocSuggestionDetail other = (CatLocSuggestionDetail) obj;
        return this.catLoc.getId()
                == other.catLoc.getId();
    }
}
