package cz.poptavka.sample.domain.demand;

/**
 * TODO martinka: add all relevant demand statuses.
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 */
public enum DemandStatus {

    NEW("NEW");


    private final String value;

    DemandStatus(String value) {
        this.value = value;
    }
}
