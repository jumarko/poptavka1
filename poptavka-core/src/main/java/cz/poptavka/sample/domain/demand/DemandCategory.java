package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@Entity
@Table(name = "DEMAND_CATEGORY")
@NamedQueries({
        @NamedQuery(name = "getDemandsForCategories", query = "select demandCategory.demand"
                        + DemandCategory.DEMANDS_FOR_CATEGORIES_FW_CLAUSE),
        @NamedQuery(name = "getDemandsCountForCategories", query = "select count(distinct demandCategory.demand)"
                        + DemandCategory.DEMANDS_FOR_CATEGORIES_FW_CLAUSE)
})
public class DemandCategory extends DomainObject {

    static final String DEMANDS_FOR_CATEGORIES_FW_CLAUSE = " from DemandCategory demandCategory"
                        + " where demandCategory.category.id in (:categoriesIds)";

    @ManyToOne
    private Demand demand;

    @ManyToOne
    private Category category;


    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DemandCategory");
        sb.append("{demand=").append(demand);
        sb.append(", category=").append(category);
        sb.append('}');
        return sb.toString();
    }
}
