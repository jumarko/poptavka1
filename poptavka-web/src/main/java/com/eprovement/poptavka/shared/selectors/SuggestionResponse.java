/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.selectors;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;

/**
 * Pack city search results with request id.
 * Since server communication is asynchronous, for long requests responses can change order.
 * And that is the problem with address selector module.
 * Each next request is usually more selective and takes less time to finnish.
 * That causes strange behaviour of replacing responses on frontend UI.
 * Therefore we need to link request and response and throw away the older one = ID.
 *
 * @author Martin Slavkovsky
 */
public class SuggestionResponse<T> implements IsSerializable {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private int id;
    private ArrayList<T> suggestions;

    /**************************************************************************/
    /*  Constructors                                                          */
    /**************************************************************************/
    public SuggestionResponse() {
        //for serialization
    }

    public SuggestionResponse(int id, ArrayList<T> suggestions) {
        this.id = id;
        this.suggestions = suggestions;
    }

    /**************************************************************************/
    /*  Getters                                                               */
    /**************************************************************************/
    public int getId() {
        return id;
    }

    public ArrayList<T> getSuggestions() {
        return suggestions;
    }

}
