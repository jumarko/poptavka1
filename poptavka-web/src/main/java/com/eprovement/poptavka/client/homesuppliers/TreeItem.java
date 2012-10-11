/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.homesuppliers;

/**
 *
 * @author Martin
 */
public class TreeItem {

    private long categoryId;
    private int level;
    private int index;

    public TreeItem(long categoryId, int level, int index) {
        this.categoryId = categoryId;
        this.level = level;
        this.index = index;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
