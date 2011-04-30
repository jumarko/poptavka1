package cz.poptavka.sample.shared.domain;

import java.io.Serializable;

public class CategoryDetail implements Serializable {

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

    private void init(Long id, String name, long demands, long suppliers) {
        this.id = id;
        this.name = name;
        this.demands = demands;
        this.suppliers = suppliers;
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

}
