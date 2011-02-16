package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.demand.Demand;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Excalibur
 * @author Juraj Martinka
 */
@Entity
//@org.hibernate.annotations.Table(comment = )
@Audited
public class Client extends BusinessUser {

    @OneToMany(mappedBy = "client")
    private List<Demand> demands;


    public List<Demand> getDemands() {
        return demands;
    }

    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }

}
