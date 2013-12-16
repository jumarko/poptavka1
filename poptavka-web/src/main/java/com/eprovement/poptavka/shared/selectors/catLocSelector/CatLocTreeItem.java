/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.selectors.catLocSelector;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Martin
 */
public class CatLocTreeItem implements IsSerializable {

    private ICatLocDetail catLoc;
    private int index;

    public CatLocTreeItem() {
        //for serialization
    }

    public CatLocTreeItem(ICatLocDetail catLoc, int index) {
        this.catLoc = catLoc;
        this.index = index;
    }

    public ICatLocDetail getCatLoc() {
        return catLoc;
    }

    public void setCategory(ICatLocDetail category) {
        this.catLoc = category;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

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
        final CatLocTreeItem other = (CatLocTreeItem) obj;
        if (this.catLoc.getId() != other.catLoc.getId()) {
            return false;
        }
        return true;
    }
}
