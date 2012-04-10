package cz.poptavka.sample.shared.domain;

import java.io.Serializable;

public class CategoryDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8677227400869014970L;

    private long id;
    private String name;
    private long demands;
    private long suppliers;
    //if parent = false, no child category exists;
    private boolean parent = false;
    private String parentName = "";

    public CategoryDetail() {
    }

    public CategoryDetail(Long id, String name) {
        init(id, name, 0, 0);
    }

    public CategoryDetail(Long id, String name, long demands, long suppliers) {
        init(id, name, demands, suppliers);
    }

    private void init(Long catId, String nameString, long demandCount, long supplierCount) {
        this.id = catId;
        this.name = nameString;
        this.demands = demandCount;
        this.suppliers = supplierCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return name + parentName;
    }

    public void setName(String name) {
        this.name = name;
    }
    public long getDemands() {
        return demands;
    }

    public void setDemands(long demands) {
        this.demands = demands;
    }

    public long getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(long suppliers) {
        this.suppliers = suppliers;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean isParent) {
        this.parent = isParent;
        if (parent) {
            this.parentName = " >";
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
